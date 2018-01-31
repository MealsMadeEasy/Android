package com.mealsmadeeasy

import android.app.Application
import android.content.Context
import android.support.v4.app.Fragment
import com.mealsmadeeasy.inject.DaggerMealsComponent
import com.mealsmadeeasy.inject.MealsComponent
import com.mealsmadeeasy.inject.MealsModule
import net.danlew.android.joda.JodaTimeAndroid

class MealsApplication : Application() {

    private lateinit var component: MealsComponent

    companion object {

        fun component(context: Context?) = (context?.applicationContext as? MealsApplication).let {
            it?.component ?: throw RuntimeException("Cannot access component from $it")
        }

        fun component(fragment: Fragment) = component(fragment.context)

    }

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)

        component = DaggerMealsComponent.builder()
                .mealsModule(MealsModule(this))
                .build()
    }

}