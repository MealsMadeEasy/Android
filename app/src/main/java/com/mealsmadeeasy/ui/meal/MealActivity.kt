package com.mealsmadeeasy.ui.meal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.Recipe
import com.mealsmadeeasy.ui.BaseActivity
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import javax.inject.Inject

class MealActivity : BaseActivity() {

    @Inject lateinit var mealStore: MealStore

    companion object {
        private const val TAG = "AddToPlanActivity"
        fun newIntent(context: Context?, id: String) = Intent(context, MealActivity::class.java)
                .putExtra("meal_id", id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        setContentView(R.layout.activity_meal)

        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.meal_page_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mealStore.findMealById(intent.getStringExtra("meal_id"))
                .bindToLifecycle(this)
                .subscribe({meal ->
                    mealSetup(meal)
                }, { throwable ->
                    Log.e(TAG, "Failed to load meal", throwable)
                    Toast.makeText(this, R.string.meal_page_failed_to_load_meal, Toast.LENGTH_SHORT).show()
                })
    }

    private fun mealSetup(meal: Meal) {
        supportActionBar?.title = meal.name

        val thumbnailView = findViewById<ImageView>(R.id.meal_thumbnail)
        val nameView = findViewById<TextView>(R.id.meal_name)
        Picasso.with(this)
                .load(meal.thumbnailUrl)
                .fit()
                .centerCrop()
                .into(thumbnailView)
        nameView.text = meal.name

        val addButton = findViewById<FloatingActionButton>(R.id.meal_add_to_plan)
        addButton.setOnClickListener {
            startActivity(AddToPlanActivity.newIntent(this, meal.id))
        }

        mealStore.getRecipe(meal.id)
                .bindToLifecycle(this)
                .subscribe({recipe ->
                    recipeSetup(recipe)
                }, { throwable ->
                    Log.e(TAG, "Failed to load recipe", throwable)
                    Toast.makeText(this, R.string.meal_page_failed_to_load_recipe, Toast.LENGTH_SHORT).show()
                })
    }

    private fun recipeSetup(recipe: Recipe) {
        val prepView = findViewById<TextView>(R.id.meal_page_prep_time)
        val stepsView = findViewById<TextView>(R.id.meal_page_steps)
        val ingredientsView = findViewById<TextView>(R.id.meal_page_ingredients)

        val prepTime = recipe.prepTime.toString() + " " + getString(R.string.meal_page_minutes)
        prepView.text = prepTime

        val newLine = System.getProperty("line.separator")

        var stepsList = ""
        recipe.steps.forEach{step ->
            stepsList += " • " +
                step.stepDescription +
                newLine +
                newLine
        }
        stepsView.text = stepsList

        var ingredientsList = ""
        recipe.ingredients.forEach{ingredient ->
            ingredientsList += ingredient.quantity.toString() + " " +
                    ingredient.unitName + " " +
                    ingredient.name +
                    newLine +
                    newLine
        }
        ingredientsView.text = ingredientsList
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}