package com.mealsmadeeasy.ui.home.grocery

import android.os.Bundle
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.ui.BaseFragment
import javax.inject.Inject

class GroceryListFragment : BaseFragment() {

    @Inject lateinit var mealStore: MealStore

    companion object {
        fun newInstance(): GroceryListFragment = GroceryListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

}