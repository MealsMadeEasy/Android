package com.mealsmadeeasy.model

import org.joda.time.DateTime

data class MealPlanEntry(
        val date: DateTime,
        val mealPeriod: MealPeriod,
        val meals: List<MealPortion>
) {

    operator fun plus(mealPortions: List<MealPortion>): MealPlanEntry {
        val originalCounts = meals.map { it.meal to it.servings }.toMap().withDefault { 0 }
        val newMeals = mealPortions.filter { new -> meals.none { it.meal == new.meal } }
        val duplicateMeals = mealPortions - newMeals
        val unmodifiedMeals = meals - duplicateMeals
        val updatedDuplicates = duplicateMeals.map { it.copy(servings = it.servings + originalCounts[it.meal]!!) }

        return copy(meals = newMeals + updatedDuplicates + unmodifiedMeals)
    }

    operator fun minus(mealPortions: List<MealPortion>): MealPlanEntry {
        val newServings = meals.map { it.meal to it.servings }.toMap()
                .mapValues { (meal, servings) ->
                    servings.toLong() - mealPortions.filter { it.meal == meal }.sumBy { it.servings }
                }
                .filterValues { it > 0 }

        return copy(meals = newServings.map { (meal, servings) -> MealPortion(meal, servings.toInt()) })
    }

}
