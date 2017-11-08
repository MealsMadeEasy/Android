package com.mealsmadeeasy.inject

import android.content.Context
import android.preference.PreferenceManager
import com.mealsmadeeasy.data.MealStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MealsModule(private val context: Context) {

    @Provides
    fun provideContext() = context

    @Provides
    fun provideSharedPrefs() = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    @Singleton
    fun provideMealStore(): MealStore = TODO()

}
