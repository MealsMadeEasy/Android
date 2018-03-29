package com.mealsmadeeasy.data.service

import com.mealsmadeeasy.data.service.model.SparseMealPlan
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPlan
import com.mealsmadeeasy.model.Recipe
import com.mealsmadeeasy.model.UserProfile
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

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

    @GET("/user/plan")
    fun getMealPlan(
            @Header("Authorization") token: String
    ): Single<Response<MealPlan>>

    @POST("/user/plan")
    fun setMealPlan(
            @Header("Authorization") token: String,
            @Body mealPlan: SparseMealPlan
    ): Single<Response<Unit>>

    @GET("/meals/suggested")
    fun getSuggestedMeals(
            @Header("Authorization") token: String
    ): Single<Response<List<Meal>>>

    @GET("/meal/{id}")
    fun getMeal(
            @Path("id") mealId: String
    ): Single<Response<Meal>>

    @GET("/recipe/{id}")
    fun getRecipe(
            @Path("id") mealId: String
    ): Single<Response<Recipe>>

}