package com.mealsmadeeasy.data

import com.mealsmadeeasy.model.Ingredient
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.model.MealPlan
import io.reactivex.Observable
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
                            .flatMapSingle { getIngredientsForRecipe(it).first(emptyList()) }
                            .collect<MutableList<Ingredient>>(::mutableListOf, { all, meal ->
                                all.addAll(meal)
                            })
                            .toObservable()
                }
                .map { it.sortedBy(Ingredient::name) }
    }

    fun addMealToMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod)

    fun removeMealFromMealPlan(meal: Meal, date: DateTime, mealPeriod: MealPeriod)

}