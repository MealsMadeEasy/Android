package com.mealsmadeeasy.data.service.model

import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.model.MealPlanEntry
import org.joda.time.DateTime

data class SparseMealPlanEntry(
        val date: DateTime,
        val mealPeriod: MealPeriod,
        val meals: List<SparseMealPortion>
) {

    constructor(mealPlanEntry: MealPlanEntry): this(
            date = mealPlanEntry.date,
            mealPeriod = mealPlanEntry.mealPeriod,
            meals = mealPlanEntry.meals.map { SparseMealPortion(it) }
    )

}