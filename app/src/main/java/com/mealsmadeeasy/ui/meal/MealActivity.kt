package com.mealsmadeeasy.ui.meal

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.ui.BaseActivity
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MealActivity : BaseActivity() {

    @Inject lateinit var mealStore: MealStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        setContentView(R.layout.activity_meal)

        val meal = mealStore.findMealById(intent.getStringExtra("meal_id"))
        val thumbnailView = findViewById<ImageView>(R.id.meal_thumbnail)
        val nameView = findViewById<TextView>(R.id.meal_name)
        val descriptionView = findViewById<TextView>(R.id.meal_description)
        Picasso.with(this)
                .load(meal.thumbnailUrl)
                .fit()
                .centerCrop()
                .into(thumbnailView)
        nameView.text = meal.name
        descriptionView.text = meal.description

        val addButton = findViewById<android.support.design.widget.FloatingActionButton>(R.id.meal_add_to_plan)
        addButton.setOnClickListener {
            val intent = Intent(this, AddToPlanActivity::class.java)
            intent.putExtra("meal_id", meal.id)
            startActivity(intent)
        }
    }
}