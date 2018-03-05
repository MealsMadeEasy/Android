package com.mealsmadeeasy.ui.home.glance

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.model.MealPortion

class WeekAtAGlanceMealPeriodViewHolder(
        root: View,
        private val onDeleteMeal: (MealPortion, MealPeriod) -> Unit,
        private val onUpdateMeal: (MealPortion, MealPeriod) -> Unit
) : RecyclerView.ViewHolder(root) {

    private lateinit var mealPeriod: MealPeriod
    private val periodName: TextView = root.findViewById(R.id.week_at_a_glance_meal_period_name)
    private val mealList: RecyclerView = root.findViewById(R.id.week_at_a_glance_meal_period_entries)
    private val mealAdapter = WeekAtAGlanceMealAdapter(emptyList(), {
        onDeleteMeal(it, mealPeriod)
    }, {
        onUpdateMeal(it, mealPeriod)
    })

    init {
        mealList.adapter = mealAdapter
        mealList.layoutManager = LinearLayoutManager(root.context)
    }

    fun bind(mealPeriod: MealPeriod, meals: List<MealPortion>) {
        this.mealPeriod = mealPeriod
        periodName.setText(mealPeriod.title)
        mealAdapter.meals = meals
    }

}