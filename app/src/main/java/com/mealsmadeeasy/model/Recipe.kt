package com.mealsmadeeasy.model

data class Recipe(
        val prepTime: Int, // Minutes
        val steps: List<RecipeStep>,
        val ingredients: List<Ingredient>
)

data class RecipeStep(
        val stepDescription: String
)