package com.mealsmadeeasy.inject

import android.content.Context
import android.preference.PreferenceManager
import com.mealsmadeeasy.data.FakeMealStore
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.data.UserManager
import com.mealsmadeeasy.data.service.MealsMadeEasyService
import com.mealsmadeeasy.data.service.createMealsMadeEasyApi
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
    fun provideMealStore(): MealStore = FakeMealStore()

    @Provides
    @Singleton
    fun provideUserManager(service: MealsMadeEasyService): UserManager = UserManager(service)

    @Provides
    @Singleton
    fun provideMealsMadeEasyService(): MealsMadeEasyService = createMealsMadeEasyApi()

}
