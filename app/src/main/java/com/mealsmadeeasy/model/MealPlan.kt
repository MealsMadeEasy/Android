package com.mealsmadeeasy.model

import org.joda.time.DateTime

data class MealPlan(
        val meals: List<MealPlanEntry>
) {

    operator fun get(date: DateTime): Map<MealPeriod, List<MealPortion>> {
        return meals.filter { it.date.withZone(date.zone).withTimeAtStartOfDay() == date.withTimeAtStartOfDay() }
                .map { it.mealPeriod to it.meals }
                .toMap()
    }

}