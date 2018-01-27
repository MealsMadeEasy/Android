package com.mealsmadeeasy.model

import android.support.annotation.StringRes
import com.mealsmadeeasy.R

enum class Gender(@StringRes val gender: Int) {

    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
    OTHER(R.string.gender_other),
    UNDISCLOSED(R.string.gender_undisclosed);

    companion object {
        fun getByString(value: String): Gender {
            for (type in Gender.values()) {
                if (type.toString() == value) {
                    return type
                }
            }
            throw IllegalArgumentException(value + " is not a valid gender")
        }
    }

}