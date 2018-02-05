package com.mealsmadeeasy.data

import com.google.firebase.auth.FirebaseAuth
import com.mealsmadeeasy.data.service.MealsMadeEasyService
import com.mealsmadeeasy.model.UserProfile
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.Exceptions
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.concurrent.Semaphore

class UserManager(private val service: MealsMadeEasyService) {

    private var profile: Single<UserProfile>? = null
    private var profileUpdateStatus: Single<Boolean>? = null

    fun getUserToken(): Single<String> = Single.fromCallable {
        val user = FirebaseAuth.getInstance().currentUser
                ?: throw IllegalStateException("User is not logged in")

        val semaphore = Semaphore(0)
        var key: String? = null

        user.getIdToken(true).addOnCompleteListener {
            if (it.isSuccessful) {
                key = it.result.token
            } else {
                throw it.exception!!
            }
            semaphore.release()
        }

        semaphore.acquire()
        return@fromCallable key!!
    }

    fun isUserOnboarded(): Single<Boolean> {
        return getUserProfile()
                .map { true }
                .onErrorReturn {
                    if (it is ProfileNotCreatedException) {
                        false
                    } else {
                        throw Exceptions.propagate(it)
                    }
                }
    }

    fun getUserProfile(): Single<UserProfile> {
        profile?.let {
            return it
        }

        val userProfile = getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getUserProfile(it) }
                .map {
                    if (it.isSuccessful) it.body() ?: throw ProfileNotCreatedException()
                    else throw IOException("${it.code()}: ${it.errorBody()?.string()}")
                }
                .doOnError {
                    profile = null
                }
                .observeOn(AndroidSchedulers.mainThread())
                .cache()

        profile = userProfile
        return userProfile
    }

    fun getUserProfileUpdateState(): Single<Boolean>? {
        return profileUpdateStatus
    }

    fun updateUserProfile(userProfile: UserProfile): Single<Boolean> {
        require(profileUpdateStatus == null) {
            "Cannot update the user profile. Another request is already in-flight."
        }

        return getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.setUserProfile(it, userProfile) }
                .map { it.isSuccessful }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess {
                    profileUpdateStatus = null
                }
                .cache()
    }

    /**
     * Thrown when trying to read a user profile, but the user doesn't have a profile yet because
     * they haven't completed onboarding.
     */
    class ProfileNotCreatedException : RuntimeException()

}