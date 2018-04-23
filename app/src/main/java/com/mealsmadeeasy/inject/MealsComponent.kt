package com.mealsmadeeasy.inject

import com.mealsmadeeasy.ui.home.glance.EditServingsDialogFragment
import com.mealsmadeeasy.ui.home.glance.WeekAtAGlanceFragment
import com.mealsmadeeasy.ui.home.grocery.GroceryListFragment
import com.mealsmadeeasy.ui.home.profile.ProfileFragment
import com.mealsmadeeasy.ui.home.discover.DiscoverFragment
import com.mealsmadeeasy.ui.home.discover.FilterPickerDialogFragment
import com.mealsmadeeasy.ui.home.discover.SearchActivity
import com.mealsmadeeasy.ui.login.LoginActivity
import com.mealsmadeeasy.ui.meal.AddToPlanActivity
import com.mealsmadeeasy.ui.meal.MealActivity
import com.mealsmadeeasy.ui.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [MealsModule::class])
interface MealsComponent {

    fun inject(activity: SplashActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: MealActivity)
    fun inject(activity: AddToPlanActivity)
    fun inject(activity: SearchActivity)

    fun inject(fragment: WeekAtAGlanceFragment)
    fun inject(fragment: ProfileFragment)
    fun inject(fragment: GroceryListFragment)
    fun inject(fragment: DiscoverFragment)
    fun inject(fragment: EditServingsDialogFragment)
    fun inject(fragment: FilterPickerDialogFragment)
}