package com.mealsmadeeasy.data

import com.mealsmadeeasy.data.service.MealsMadeEasyService
import com.mealsmadeeasy.model.UserProfile
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.io.IOException

class UserManager(private val service: MealsMadeEasyService) {

    fun getUserToken(): Single<String> = TODO("Get this from Firebase")

    fun getUserProfile(): Single<UserProfile> {
        return getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getUserProfile(it) }
                .unwrapResponse()
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