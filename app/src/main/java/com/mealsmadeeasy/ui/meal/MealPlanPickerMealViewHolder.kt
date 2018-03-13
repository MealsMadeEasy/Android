package com.mealsmadeeasy.ui.meal

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.MealPortion
import com.squareup.picasso.Picasso

class MealPlanPickerMealViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private lateinit var mealPortion : MealPortion
    private val mealName = root.findViewById<TextView>(R.id.week_at_a_glance_meal_name)
    private val mealImage = root.findViewById<ImageView>(R.id.week_at_a_glance_meal_background)
    private val menu = root.findViewById<ImageView>(R.id.week_at_a_glance_menu)

    fun bind(meal: MealPortion) {
        mealPortion = meal
        mealName.text = mealPortion.meal.name
        menu.visibility = View.GONE
        if (mealPortion.meal.thumbnailUrl != null) {
            mealImage.visibility = View.VISIBLE
            Picasso.with(itemView.context)
                    .load(mealPortion.meal.thumbnailUrl)
                    .into(mealImage)
        } else {
            mealImage.visibility = View.GONE
        }
    }
}