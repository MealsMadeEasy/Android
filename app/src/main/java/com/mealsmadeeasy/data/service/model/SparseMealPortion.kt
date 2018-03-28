package com.mealsmadeeasy.data.service.model

import com.mealsmadeeasy.model.MealPortion

data class SparseMealPortion(
        val mealId: String,
        val servings: Int
) {

    constructor(mealPortion: MealPortion): this(
            mealId = mealPortion.meal.id,
            servings = mealPortion.servings
    )

}