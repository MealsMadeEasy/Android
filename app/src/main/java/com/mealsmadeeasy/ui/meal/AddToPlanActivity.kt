package com.mealsmadeeasy.ui.meal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.*
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.ui.BaseActivity
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddToPlanActivity : BaseActivity() {

    @Inject lateinit var mealStore: MealStore

    companion object {
        private const val TAG = "AddToPlanActivity"
        fun newIntent(context: Context, id: String) = Intent(context, AddToPlanActivity::class.java)
                .putExtra("meal_id", id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        setContentView(R.layout.activity_add_to_plan)

        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.add_to_plan_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mealStore.findMealById(intent.getStringExtra("meal_id"))
                .bindToLifecycle(this)
                .subscribe({meal ->
                    onMealLoaded(meal)
                }, { throwable ->
                    Log.e(TAG, "Failed to load suggestions", throwable)
                    Toast.makeText(this, "Couldn't find that meal", Toast.LENGTH_SHORT).show()
                })
    }

    private fun onMealLoaded(meal: Meal) {
        val thumbnailView = findViewById<ImageView>(R.id.add_to_plan_thumbnail)
        val nameView = findViewById<TextView>(R.id.add_to_plan_name)
        val servingsView = findViewById<TextView>(R.id.add_to_plan_servings)
        val minusButton = findViewById<ImageView>(R.id.add_to_plan_minus)
        val plusButton = findViewById<ImageView>(R.id.add_to_plan_plus)
        val addButton = findViewById<Button>(R.id.add_to_plan_button)

        Picasso.with(this)
                .load(meal.thumbnailUrl)
                .fit()
                .centerCrop()
                .into(thumbnailView)
        nameView.text = meal.name

        servingsView.text = "1"
        var numServings = 1
        minusButton.isEnabled = false

        minusButton.setOnClickListener {
            if (numServings != 1) {
                numServings--
                servingsView.text = numServings.toString()
                minusButton.isEnabled = (numServings > 1)
            }
        }

        plusButton.setOnClickListener {
            numServings++
            minusButton.isEnabled = (numServings > 1)
            servingsView.text = numServings.toString()
        }


        val rv = findViewById<RecyclerView>(R.id.add_to_plan_calendar)
        rv.layoutManager = LinearLayoutManager(this)

        mealStore.getMealPlan()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .subscribe({ mealPlan ->
                    (rv.adapter as? MealPlanPickerAdapter).let {
                        if (it == null) {
                            rv.adapter = MealPlanPickerAdapter(mealPlan = mealPlan)
                        } else {
                            it.mealPlan = mealPlan
                        }
                    }
                })

//        addButton.setOnClickListener {
//            if () {
//                Toast.makeText(this, R.string.add_to_plan_date_not_selected, Toast.LENGTH_SHORT).show()
//            } else {
//
//
//                mealStore.addMealToMealPlan(meal, DateTime(chosenTime), mealPeriod, numServings)
//                Toast.makeText(this, R.string.add_to_plan_meal_added, Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}