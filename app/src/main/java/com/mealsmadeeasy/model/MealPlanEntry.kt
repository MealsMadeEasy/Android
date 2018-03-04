package com.mealsmadeeasy.model

import com.squareup.moshi.Json
import org.joda.time.DateTime

data class MealPlanEntry(
        @Json(name = "date") val timestamp: Timestamp,
        val mealPeriod: MealPeriod,
        val meals: List<MealPortion>
) {

    constructor(date: DateTime, mealPeriod: MealPeriod, meals: List<MealPortion>):
            this(date.millis, mealPeriod, meals)

    val date: DateTime
        get() = timestamp.toDate()

}
