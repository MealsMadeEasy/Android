package com.mealsmadeeasy.ui.home.glance

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.support.v7.widget.PopupMenu
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Meal
import com.squareup.picasso.Picasso
import android.view.MenuItem


class WeekAtAGlanceMealViewHolder(
        root: View,
        private val onDeleteMeal: (Meal) -> Unit
) : RecyclerView.ViewHolder(root) {

    private lateinit var meal : Meal
    private val mealName = root.findViewById<TextView>(R.id.week_at_a_glance_meal_name)
    private val mealImage = root.findViewById<ImageView>(R.id.week_at_a_glance_meal_background)

    init {
        val menu = root.findViewById<ImageView>(R.id.week_at_a_glance_menu)
        menu.setOnClickListener { onClickPopupMenu(menu) }
    }

    fun bind(meal: Meal) {
        this.meal = meal
        mealName.text = meal.name
        if (meal.thumbnailUrl != null) {
            mealImage.visibility = View.VISIBLE
            Picasso.with(itemView.context)
                    .load(meal.thumbnailUrl)
                    .into(mealImage)
        } else {
            mealImage.visibility = View.GONE
        }
    }

    private fun onMenuItemClick(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menu_item_delete_meal -> {
                onDeleteMeal(meal)
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun onClickPopupMenu(menu : ImageView) {
        val popup = PopupMenu(itemView.context!!, menu, Gravity.END)
        popup.menuInflater.inflate(R.menu.instance_meal, popup.menu)
        popup.setOnMenuItemClickListener({ menuItem ->
            onMenuItemClick(menuItem)
        })
        popup.show()
    }
}