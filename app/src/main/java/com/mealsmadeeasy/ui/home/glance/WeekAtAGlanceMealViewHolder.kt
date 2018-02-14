package com.mealsmadeeasy.ui.home.glance

import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
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
        menu.setOnClickListener {
            val popup = PopupMenu(itemView.context!!, itemView, Gravity.END)
            popup.menuInflater.inflate(R.menu.instance_meal, popup.menu)
            popup.setOnMenuItemClickListener({ menuItem ->
                onMenuItemClick(menuItem)
            })
            popup.show()
        }

//        val popup = PopupMenu(itemView.context!!, itemView, Gravity.END)
//        popup.menuInflater.inflate(R.menu.instance_meal, popup.menu)
//        popup.setOnMenuItemClickListener({ menuItem ->
//            onMenuItemClick(menuItem)
//        })
//        popup.show()
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
                val builder = AlertDialog.Builder(itemView.context!!)
                builder.setPositiveButton(
                        itemView.context.getString(R.string.week_at_a_glance_delete_prompt_positive_ack),  {_, _ ->
                    onDeleteMeal(meal)
                })
                builder.setNegativeButton(
                        itemView.context.getString(R.string.week_at_a_glance_delete_prompt_negative_ack), { _, _ ->
                    // Do nothing.
                })
                builder.setMessage(itemView.context.getString(R.string.week_at_a_glance_delete_prompt))
                builder.show()
                return true
            }
            else -> {
                return false
            }
        }
    }
}