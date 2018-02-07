package com.mealsmadeeasy.data.service

import com.mealsmadeeasy.model.UserProfile
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MealsMadeEasyService {

    @GET("user/profile")
    fun getUserProfile(
            @Header("Authorization") token: String
    ): Single<Response<UserProfile?>>

    @POST("user/profile")
    fun setUserProfile(
            @Header("Authorization") token: String,
            @Body profile: UserProfile
    ): Single<Response<Unit>>

}