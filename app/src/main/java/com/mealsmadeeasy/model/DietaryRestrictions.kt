package com.mealsmadeeasy.model

import android.support.annotation.StringRes
import com.mealsmadeeasy.R

enum class DietaryRestrictions(@StringRes val restriction: Int) {
    NONE(R.string.diet_none),
    PEANUT(R.string.diet_peanut),
    MILK(R.string.diet_milk),
    EGG(R.string.diet_egg),
    WHEAT(R.string.diet_wheat),
    SOY(R.string.diet_soy),
    FISH(R.string.diet_fish),
    SHELLFISH(R.string.diet_shellfish),
    ALCOHOL(R.string.diet_alcohol),
    MEAT(R.string.diet_meat),
    POULTRY(R.string.diet_poultry);

    companion object {
        fun getByString(value: String): DietaryRestrictions {
            for (type in DietaryRestrictions.values()) {
                if (type.toString() == value) {
                    return type
                }
            }
            throw IllegalArgumentException(value + " is not a valid dietary restriction")
        }
    }
}