package com.mealsmadeeasy.data.service.model

import com.mealsmadeeasy.model.MealPlan

data class SparseMealPlan(
        val meals: List<SparseMealPlanEntry>
) {

    constructor(mealPlan: MealPlan): this(mealPlan.meals.map { SparseMealPlanEntry(it) })

}