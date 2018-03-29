package com.mealsmadeeasy.model

typealias MealId = String

data class Meal (
        val id: MealId,
        val name: String,
        val description: String,
        val thumbnailUrl: String?
)
