package com.mealsmadeeasy.model

import org.joda.time.DateTime

data class MealPlanEntry(
        val date: DateTime,
        val mealPeriod: MealPeriod,
        var meals: List<Meal>
)
