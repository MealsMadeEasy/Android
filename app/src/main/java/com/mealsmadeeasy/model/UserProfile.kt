package com.mealsmadeeasy.model

private const val INCHES_TO_METERS = 0.0254
private const val POUNDS_TO_KILOGRAMS = 0.453592

data class UserProfile(
        val gender: Gender,
        val birthday: Timestamp,
        val height: Inches,
        val weight: Pounds
) {
    val bmi: Double
        get() = poundsToKilograms(weight) / heightToMeters(height)

    private fun heightToMeters(inches: Int): Double {
        return inches * INCHES_TO_METERS
    }

    private fun poundsToKilograms(pounds: Int): Double {
        return pounds * POUNDS_TO_KILOGRAMS
    }
}
