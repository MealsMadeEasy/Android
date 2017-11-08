package com.mealsmadeeasy.model

import android.support.annotation.StringRes
import com.mealsmadeeasy.R

enum class MealPeriod(@StringRes val title: Int) {

    BREAKFAST(R.string.meal_breakfast),
    LUNCH(R.string.meal_lunch),
    DINNER(R.string.meal_dinner),

    ;

}