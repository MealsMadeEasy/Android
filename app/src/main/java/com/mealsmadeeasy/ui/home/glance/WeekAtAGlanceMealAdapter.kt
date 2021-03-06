package com.mealsmadeeasy.ui.home.glance

import android.support.v4.app.DialogFragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.MealPortion

class WeekAtAGlanceMealAdapter(
        meals: List<MealPortion> = emptyList(),
        private val onDeleteMeal: (MealPortion) -> Unit,
        private val servingsFragment: (MealPortion) -> DialogFragment
) : RecyclerView.Adapter<WeekAtAGlanceMealViewHolder>() {

    var meals: List<MealPortion> = meals
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = meals.size

    override fun onBindViewHolder(holder: WeekAtAGlanceMealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekAtAGlanceMealViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(
                R.layout.view_week_at_a_glance_meal, parent, false)
        return WeekAtAGlanceMealViewHolder(root, onDeleteMeal, servingsFragment)
    }

}