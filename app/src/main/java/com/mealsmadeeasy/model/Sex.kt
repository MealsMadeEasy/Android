package com.mealsmadeeasy.model

import android.support.annotation.StringRes
import com.mealsmadeeasy.R

enum class Sex(@StringRes val sex: Int) {
    MALE(R.string.sex_male),
    FEMALE(R.string.sex_female),
    OTHER(R.string.sex_other)
    ;
}