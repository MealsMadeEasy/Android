package com.mealsmadeeasy.ui.home.glance

import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPeriod
import com.squareup.picasso.Picasso
import org.joda.time.DateTime
import javax.inject.Inject

class WeekAtAGlanceMealViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    @Inject lateinit var mealStore: MealStore

    private lateinit var meal : Meal
    private val mealName = root.findViewById<TextView>(R.id.week_at_a_glance_meal_name)
    private val mealImage = root.findViewById<ImageView>(R.id.week_at_a_glance_meal_background)

    init {
        val delete = root.findViewById<TextView>(R.id.week_at_a_glance_meal_delete)
        delete?.setOnClickListener {
            val builder = AlertDialog.Builder(itemView.context!!)
            builder.setPositiveButton(
                    itemView.context.getString(R.string.week_at_a_glance_delete_prompt_positive_ack),  {_, _ ->
                // TODO
                mealStore.removeMealFromMealPlan(meal, DateTime.now(), MealPeriod.BREAKFAST)
            })
            builder.setNegativeButton(
                    itemView.context.getString(R.string.week_at_a_glance_delete_prompt_negative_ack), { _, _ ->
                // Do nothing.
            })
            builder.setMessage(itemView.context.getString(R.string.week_at_a_glance_delete_prompt))
            builder.show()
        }
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

}