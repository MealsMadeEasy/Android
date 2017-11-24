package com.mealsmadeeasy.ui.home.glance

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPeriod
import org.joda.time.DateTime

class WeekAtAGlanceViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val day: TextView = root.findViewById(R.id.week_at_a_glance_day_date)
    private val month: TextView = root.findViewById(R.id.week_at_a_glance_day_month)

    fun bind(date: DateTime, meals: Map<MealPeriod, List<Meal>>) {
        day.text = date.dayOfMonth.toString()
        month.text = date.monthOfYear().asShortText
    }

}