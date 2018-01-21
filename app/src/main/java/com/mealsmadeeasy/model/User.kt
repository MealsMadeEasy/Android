package com.mealsmadeeasy.model

data class User (
        val sex: Sex,
        val age: Int,
        val height: Double,
        val weight: Double,
        val restrictions: List<DietaryRestrictions>
)