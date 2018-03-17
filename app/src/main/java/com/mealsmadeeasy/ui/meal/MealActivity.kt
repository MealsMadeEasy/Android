package com.mealsmadeeasy.ui.meal

import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.mealsmadeeasy.utils.PicassoImageGetter
import com.mealsmadeeasy.utils.whenMeasured
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
                    onMealLoaded(meal)
                }, { throwable ->
                    Log.e(TAG, "Failed to load meal", throwable)
                    Toast.makeText(this, R.string.meal_page_failed_to_load_meal, Toast.LENGTH_SHORT).show()
                })
    }

    private fun onMealLoaded(meal: Meal) {
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
                    onRecipeLoaded(recipe)
                }, { throwable ->
                    Log.e(TAG, "Failed to load recipe", throwable)
                    Toast.makeText(this, R.string.meal_page_failed_to_load_recipe, Toast.LENGTH_SHORT).show()
                })
    }

    private fun onRecipeLoaded(recipe: Recipe) {
        findViewById<TextView>(R.id.meal_page_steps).setHtml(recipe.content)
    }

    private fun TextView.setHtml(html: String) {
        whenMeasured {
            val imageGetter =  PicassoImageGetter(context,
                    maxWidth = measuredWidth - paddingStart - paddingEnd,
                    onChangeCallback = { text = text })

            text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, imageGetter, null)
            } else {
                @Suppress("DEPRECATION")
                Html.fromHtml(html, imageGetter, null)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}