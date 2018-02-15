package com.mealsmadeeasy.ui.meal

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TextView
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Button
import android.widget.Toast
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.ui.BaseActivity
import com.squareup.picasso.Picasso
import org.joda.time.DateTime
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class AddToPlanActivity : BaseActivity() {

    @Inject lateinit var mealStore: MealStore
    private var cal = Calendar.getInstance()
    var chosenTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        setContentView(R.layout.activity_add_to_plan)

        val meal = mealStore.findMealById(intent.getStringExtra("meal_id"))
        val thumbnailView = findViewById<ImageView>(R.id.add_to_plan_thumbnail)
        val nameView = findViewById<TextView>(R.id.add_to_plan_name)
        val servingsView = findViewById<TextView>(R.id.add_to_plan_servings)
        val minusButton = findViewById<ImageButton>(R.id.add_to_plan_minus)
        val plusButton = findViewById<ImageButton>(R.id.add_to_plan_plus)
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
                if (numServings == 1) {
                    minusButton.isEnabled = false
                }
            }
        }

        plusButton.setOnClickListener {
            if (numServings == 1) {
                minusButton.isEnabled = true
            }
            numServings++
            servingsView.text = numServings.toString()
        }

        setupDatePicker(dateView)

        addButton.setOnClickListener {
            if (dateView.text == "") {
                Toast.makeText(this, "Choose a date for the meal", Toast.LENGTH_SHORT).show()
            } else {

                var mealPeriod = MealPeriod.BREAKFAST
                when (mealSpinner.selectedItem) {
                    "Lunch" -> mealPeriod = MealPeriod.LUNCH
                    "Dinner" -> mealPeriod = MealPeriod.DINNER
                }

                mealStore.addMealToMealPlan(meal, DateTime(chosenTime), mealPeriod, numServings)
                Toast.makeText(this, "Meal has been added to your meal plan", Toast.LENGTH_SHORT).show()
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
}