package com.mealsmadeeasy.model

import org.joda.time.DateTime

data class MealPlanEntry(
        val date: DateTime,
        val mealPeriod: MealPeriod,
        val meals: List<MealPortion>
) {



}
