package com.mealsmadeeasy.model

import org.joda.time.DateTime

data class MealPlan(
        val meals: List<MealPlanEntry>
) {

    operator fun get(date: DateTime): Map<MealPeriod, List<Meal>> {
        return meals.filter { it.date.withTimeAtStartOfDay() == date.withTimeAtStartOfDay() }
                .map { it.mealPeriod to it.meals }
                .toMap()
    }

}