package com.mealsmadeeasy.model

data class UserProfile(
        val gender: Gender,
        val birthday: Timestamp,
        val height: Inches,
        val weight: Pounds
)
