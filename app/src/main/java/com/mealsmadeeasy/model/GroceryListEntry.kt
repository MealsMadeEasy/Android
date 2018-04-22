package com.mealsmadeeasy.model

data class GroceryListEntry(
        val ingredient: Ingredient,
        val purchased: Boolean,
        val dependants: List<String>
)
