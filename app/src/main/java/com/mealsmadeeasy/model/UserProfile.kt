package com.mealsmadeeasy.model

const val FEET_TO_METERS = 0.3048
const val INCHES_TO_METERS = 0.0254
const val POUNDS_TO_KILOGGRAMS = 0.453592

data class UserProfile(
        val gender: Gender,
        val birthday: Timestamp,
        val height: Inches,
        val weight: Pounds
) {
    companion object {
        fun calculateBMI(feet: Int, inches: Int, pounds: Int): Double {
            return poundsToKilograms(pounds) / heightToMeters(feet, inches)
        }

        private fun heightToMeters(feet: Int, inches: Int): Double {
            return feet * FEET_TO_METERS + inches * INCHES_TO_METERS
        }

        private fun poundsToKilograms(pounds: Int): Double {
            return pounds * POUNDS_TO_KILOGGRAMS
        }
    }
}
