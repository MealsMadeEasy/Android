package com.mealsmadeeasy.inject

import com.mealsmadeeasy.ui.home.glance.WeekAtAGlanceFragment
import com.mealsmadeeasy.ui.home.grocery.GroceryListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    MealsModule::class
))
interface MealsComponent {

    fun inject(fragment: WeekAtAGlanceFragment)
    fun inject(fragment: GroceryListFragment)

}