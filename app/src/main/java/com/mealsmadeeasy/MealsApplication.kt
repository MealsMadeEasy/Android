package com.mealsmadeeasy

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class MealsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }

}