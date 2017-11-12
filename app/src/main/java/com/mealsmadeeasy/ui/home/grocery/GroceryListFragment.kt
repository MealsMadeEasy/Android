package com.mealsmadeeasy.ui.home.grocery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // TODO return the root view of the grocery list here.
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}