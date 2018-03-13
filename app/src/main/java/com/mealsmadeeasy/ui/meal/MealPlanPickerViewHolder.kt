package com.mealsmadeeasy.ui.meal

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.model.MealPortion
import org.joda.time.DateTime

class MealPlanPickerViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private lateinit var dateTime: DateTime
    private val day: TextView = root.findViewById(R.id.week_at_a_glance_day_date)
    private val month: TextView = root.findViewById(R.id.week_at_a_glance_day_month)
    private val mealList: RecyclerView = root.findViewById(R.id.week_at_a_glance_day_entry_list)
    private val mealAdapter = MealPlanPickerMealPeriodAdapter(emptyMap())

    init {
        mealList.adapter = mealAdapter
        mealList.layoutManager = LinearLayoutManager(root.context)
    }

    fun bind(date: DateTime, meals: Map<MealPeriod, List<MealPortion>>) {
        this.dateTime = date
        day.text = date.dayOfMonth.toString()
        month.text = date.monthOfYear().asShortText
        mealAdapter.meals = meals
    }

}