package com.mealsmadeeasy.ui.home.glance

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPeriod

class WeekAtAGlanceMealPeriodViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val periodName: TextView = root.findViewById(R.id.week_at_a_glance_meal_period_name)
    private val mealList: RecyclerView = root.findViewById(R.id.week_at_a_glance_meal_period_entries)
    private val mealAdapter = WeekAtAGlanceMealAdapter()

    init {
        mealList.adapter = mealAdapter
        mealList.layoutManager = LinearLayoutManager(root.context)
    }

    fun bind(mealPeriod: MealPeriod, meals: List<Meal>) {
        periodName.setText(mealPeriod.title)
        mealAdapter.meals = meals
    }

}