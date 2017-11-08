package com.mealsmadeeasy.inject

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    MealsModule::class
))
interface MealsComponent {

    // TODO Inject things

}