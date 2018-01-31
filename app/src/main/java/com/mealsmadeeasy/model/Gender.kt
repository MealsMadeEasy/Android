package com.mealsmadeeasy.model

import android.support.annotation.StringRes
import com.mealsmadeeasy.R

enum class Gender(@StringRes val gender: Int) {

    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
    OTHER(R.string.gender_other),
    UNDISCLOSED(R.string.gender_undisclosed);

    companion object {
        fun valuesAsStringRes(): List<Int> {
            return listOf(R.string.gender_male, R.string.gender_female, R.string.gender_other,
                    R.string.gender_undisclosed)
        }
    }
}