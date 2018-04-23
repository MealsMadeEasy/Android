package com.mealsmadeeasy.data

import com.mealsmadeeasy.model.*
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.DateTime

interface MealStore {

    fun getMealPlan(): Observable<MealPlan>

    fun getIngredientsForMealPlan(): Observable<GroceryList>

    fun markIngredientPurchased(ingredient: Ingredient, purchased: Boolean)

    fun addMealToMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int)

    fun editMealPlanServings(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int)

    fun removeMealFromMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod)

    fun getSuggestedMeals(): Single<List<Meal>>

    fun findMealById(id: String): Single<Meal>

    fun getRecipe(id: String): Single<Recipe>

    fun search(query: String, filters: List<Filter> = emptyList()): Single<List<Meal>>

    fun getAvailableFilters(): Single<List<FilterGroup>>

}