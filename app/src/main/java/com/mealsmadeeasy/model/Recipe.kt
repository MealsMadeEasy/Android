package com.mealsmadeeasy.model

data class Recipe(
        val prepTime: Minutes,
        val steps: List<RecipeStep>,
        val ingredients: List<Ingredient>
)
