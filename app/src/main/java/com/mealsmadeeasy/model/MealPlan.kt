package com.mealsmadeeasy.model

import com.mealsmadeeasy.utils.replace
import org.joda.time.DateTime

data class MealPlan(
        val meals: List<MealPlanEntry>
) {

    operator fun get(date: DateTime): Map<MealPeriod, List<MealPortion>> {
        return meals.filter { it.date.withZone(date.zone).withTimeAtStartOfDay() == date.withTimeAtStartOfDay() }
                .map { it.mealPeriod to it.meals }
                .toMap()
    }

    operator fun plus(mealPlanEntry: MealPlanEntry): MealPlan {
        val entry = meals.filter { it.date.dayOfMonth() == mealPlanEntry.date.dayOfMonth() }
                .firstOrNull { it.mealPeriod == mealPlanEntry.mealPeriod }

        return if (entry != null) {
            copy(meals = meals.replace(old = entry, new = entry + mealPlanEntry.meals))
        } else {
            copy(meals = meals + mealPlanEntry)
        }
    }

    operator fun minus(mealPlanEntry: MealPlanEntry): MealPlan {
        val entry = meals.filter { it.date.dayOfMonth() == mealPlanEntry.date.dayOfMonth() }
                .firstOrNull { it.mealPeriod == mealPlanEntry.mealPeriod }

        return if (entry != null) {
            copy(meals = meals.replace(old = entry, new = entry - mealPlanEntry.meals))
        } else {
            copy(meals = meals + mealPlanEntry)
        }
    }

}