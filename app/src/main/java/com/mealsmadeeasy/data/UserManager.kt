package com.mealsmadeeasy.data

import com.google.firebase.auth.FirebaseAuth
import com.mealsmadeeasy.data.service.MealsMadeEasyService
import com.mealsmadeeasy.model.UserProfile
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Semaphore

class UserManager(private val service: MealsMadeEasyService) {

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
        return getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getUserProfile(it) }
                .map {
                    if (it.isSuccessful) it.body() != null
                    else throw IOException("${it.code()}: ${it.errorBody()?.string()}")
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getUserProfile(): Single<UserProfile> {
        return getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getUserProfile(it) }
                .map {
                    if (it.isSuccessful) it.body()!!
                    else throw IOException("${it.code()}: ${it.errorBody()?.string()}")
                }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateUserProfile(userProfile: UserProfile): Single<Boolean> {
        return getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.setUserProfile(it, userProfile) }
                .map { it.isSuccessful }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private fun <T: Any> Single<Response<T>>.unwrapResponse(): Single<T> =
            map {
                if (it.isSuccessful) it.body()
                else throw IOException("${it.code()}: ${it.errorBody()?.string()}")
            }

}