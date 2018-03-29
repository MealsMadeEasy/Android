package com.mealsmadeeasy.data

import com.mealsmadeeasy.model.*
import io.reactivex.Observable
import io.reactivex.Single
import org.joda.time.DateTime

interface MealStore {

    fun getMealPlan(): Observable<MealPlan>

    fun getIngredientsForRecipe(meal: Meal): Observable<List<Ingredient>>

    fun getIngredientsForMealPlan(): Observable<List<Ingredient>> {
        return getMealPlan()
                .map { it.meals }
                .flatMap { meals ->
                    Observable.fromIterable(meals)
                            .flatMap { Observable.fromIterable(it.meals) }
                            .map { it.meal }
                            .flatMapSingle { getIngredientsForRecipe(it).first(emptyList()) }
                            .collect<MutableList<Ingredient>>(::mutableListOf, { all, meal ->
                                all.addAll(meal)
                            })
                            .toObservable()
                }
                .map { it.sortedBy(Ingredient::name) }
    }

    fun addMealToMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod, servings: Int)

    fun removeMealFromMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod)

    fun getSuggestedMeals(): Single<List<Meal>>

    fun findMealById(id: String): Single<Meal>

    fun getRecipe(id: String): Single<Recipe>

    fun search(query: String, filters: List<Filter> = emptyList()): Single<List<Meal>>

    fun getAvailableFilters(): Single<List<FilterGroup>>

}