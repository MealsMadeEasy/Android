package com.mealsmadeeasy.ui.home.glance

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.MealPlan
import org.joda.time.DateTime

private const val NUMBER_OF_DAYS_TO_SHOW = 7

class WeekAtAGlanceAdapter(
        startDate: DateTime = DateTime.now(),
        mealPlan: MealPlan
) : RecyclerView.Adapter<WeekAtAGlanceViewHolder>() {

    var startDate = startDate.withTimeAtStartOfDay()
        set(value) {
            field = value.withTimeAtStartOfDay()
            notifyDataSetChanged()
        }

    var mealPlan = mealPlan
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = NUMBER_OF_DAYS_TO_SHOW

    override fun onBindViewHolder(holder: WeekAtAGlanceViewHolder, position: Int) {
        val day = startDate.plusDays(position)
        holder.bind(day, mealPlan[day])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekAtAGlanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
                R.layout.view_week_at_a_glance_day, parent, false)
        return WeekAtAGlanceViewHolder(view)
    }


}