package com.mealsmadeeasy.data

import com.mealsmadeeasy.data.service.MealsMadeEasyService
import com.mealsmadeeasy.model.Ingredient
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.model.MealPlan
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import retrofit2.Response
import java.io.IOException

class NetMealStore(
        private val userManager: UserManager,
        private val service: MealsMadeEasyService
) : MealStore {

    private val mealPlan = RxLoader {
        userManager.getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getMealPlan(it) }
                .map { it.unwrap() }
    }

    private val suggestedMeals = RxLoader {
        userManager.getUserToken()
                .subscribeOn(Schedulers.io())
                .flatMap { service.getSuggestedMeals(it) }
                .map { it.unwrap() }
    }

    override fun getMealPlan(): Observable<MealPlan> {
        return mealPlan.getOrComputeValue()
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getIngredientsForRecipe(meal: Meal): Observable<List<Ingredient>> {
        TODO("not implemented")
    }

    override fun addMealToMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int) {
        TODO("not implemented")
    }

    override fun removeMealFromMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod) {
        TODO("not implemented")
    }

    override fun getSuggestedMeals(): Single<List<Meal>> {
        return suggestedMeals.getOrComputeValue()
                .first(emptyList())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun findMealById(id: String): Single<Meal> {
        TODO("not implemented")
    }

    private fun <T> Response<T>.unwrap(): T {
        if (!isSuccessful) {
            throw IOException("${code()}: ${errorBody()}")
        } else {
            return body() ?: throw IOException("No data was returned")
        }
    }

}