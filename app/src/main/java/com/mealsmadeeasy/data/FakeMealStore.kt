package com.mealsmadeeasy.data

import com.mealsmadeeasy.model.Ingredient
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPlan
import io.reactivex.Observable

class FakeMealStore : MealStore {

    override fun getMealPlan(): Observable<MealPlan> {
        TODO("not implemented")
    }

    override fun getIngredientsForRecipe(meal: Meal): Observable<List<Ingredient>> {
        TODO("not implemented")
    }


}