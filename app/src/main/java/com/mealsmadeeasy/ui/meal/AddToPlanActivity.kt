package com.mealsmadeeasy.ui.meal

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.Meal
import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.ui.BaseActivity
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import org.joda.time.DateTime
import java.util.*
import javax.inject.Inject

class AddToPlanActivity : BaseActivity() {

    @Inject lateinit var mealStore: MealStore
    private var cal = Calendar.getInstance()
    private var chosenTime: Long = 0

    companion object {
        private const val TAG = "AddToPlanActivity"
        fun newIntent(context: Context, id: String) = Intent(context, AddToPlanActivity::class.java)
                .putExtra("meal_id", id)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        setContentView(R.layout.activity_add_to_plan)

        val toolbar = findViewById<Toolbar>(R.id.add_to_plan_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        mealStore.findMealById(intent.getStringExtra("meal_id"))
                .bindToLifecycle(this)
                .subscribe({meal ->
                    finishSetup(meal)
                }, { throwable ->
                    Log.e(TAG, "Failed to load suggestions", throwable)
                    Toast.makeText(this, "Couldn't find that meal", Toast.LENGTH_SHORT).show()
                })
    }

    private fun finishSetup(meal: Meal) {
        val thumbnailView = findViewById<ImageView>(R.id.add_to_plan_thumbnail)
        val nameView = findViewById<TextView>(R.id.add_to_plan_name)
        val servingsView = findViewById<TextView>(R.id.add_to_plan_servings)
        val minusButton = findViewById<ImageView>(R.id.add_to_plan_minus)
        val plusButton = findViewById<ImageView>(R.id.add_to_plan_plus)
        val dateView = findViewById<TextView>(R.id.add_to_plan_date)
        val mealSpinner = findViewById<Spinner>(R.id.add_to_plan_spinner)
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

        setupDatePicker(dateView)

        addButton.setOnClickListener {
            if (dateView.text == "") {
                Toast.makeText(this, R.string.add_to_plan_date_not_selected, Toast.LENGTH_SHORT).show()
            } else {
                var mealPeriod = MealPeriod.BREAKFAST
                when (mealSpinner.selectedItem) {
                    "Lunch" -> mealPeriod = MealPeriod.LUNCH
                    "Dinner" -> mealPeriod = MealPeriod.DINNER
                }

                mealStore.addMealToMealPlan(meal, DateTime(chosenTime), mealPeriod, numServings)
                Toast.makeText(this, R.string.add_to_plan_meal_added, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupDatePicker(date: TextView) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            date.text = DateFormat.getDateFormat(this).format(Date(cal.timeInMillis))
            chosenTime = cal.timeInMillis
        }

        date.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}