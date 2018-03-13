package com.mealsmadeeasy.ui.meal

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.MealPortion

class MealPlanPickerMealAdapter(
        meals: List<MealPortion> = emptyList()) : RecyclerView.Adapter<MealPlanPickerMealViewHolder>() {

    var meals: List<MealPortion> = meals
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = meals.size

    override fun onBindViewHolder(holder: MealPlanPickerMealViewHolder, position: Int) {
        holder.bind(meals[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealPlanPickerMealViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(
                R.layout.view_week_at_a_glance_meal, parent, false)
        return MealPlanPickerMealViewHolder(root)
    }

}