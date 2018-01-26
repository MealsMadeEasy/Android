package com.mealsmadeeasy.ui.home.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Sex
import com.mealsmadeeasy.ui.BaseFragment

import java.util.*
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat


const val MIN_AGE = 13
const val INCHES_IN_FEET = 12
const val MIN_BMI = 15
const val MAX_BMI = 50

const val FEET_TO_METERS = 0.3048
const val INCHES_TO_METERS = 0.0254
const val POUNDS_TO_KILOGGRAMS = 0.453592

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()

        fun calculateBMI(feet: Int, inches: Int, pounds: Int): Double {
            return poundsToKilograms(pounds) / heightToMeters(feet, inches)
        }

        fun heightToMeters(feet: Int, inches: Int): Double {
            return feet * FEET_TO_METERS + inches * INCHES_TO_METERS
        }

        fun poundsToKilograms(pounds: Int): Double {
            return pounds * POUNDS_TO_KILOGGRAMS
        }

        fun heightToInches(feet: Int, inches: Int): Int {
            return feet * INCHES_IN_FEET + inches
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.view_user_profile, container, false)

        // Populate sex spinner values
        val sexSpinner = root.findViewById<Spinner>(R.id.sex_spinner)
        val sexValues: List<String> = listOf(
                Sex.MALE.toString(), Sex.FEMALE.toString(), Sex.OTHER.toString(), "Sex")
        val sexAdapter = HintAdapter(this.activity, android.R.layout.simple_list_item_1, sexValues)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = sexAdapter
        sexSpinner.setSelection(sexAdapter.count)

        // Set up birthday date picker
        val dateText = root.findViewById<EditText>(R.id.birthday_edit_text)
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            dateText.setText(SimpleDateFormat("dd.MM.yyyy").format(cal.time))

        }

        dateText.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Set save button behavior
        val saveButton = root.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            val sex = sexSpinner.selectedItem
            val age = cal.timeInMillis
            val feet = root.findViewById<EditText>(R.id.height_feet_field).text.toString()
            val inches = root.findViewById<EditText>(R.id.height_inches_field).text.toString()
            val pounds = root.findViewById<EditText>(R.id.weight_field).text.toString()

            val builder = AlertDialog.Builder(this.activity)
            if (feet == "" || inches == "" || pounds == "") {
                builder.setMessage(R.string.error_profile_blank_field)
            } else if (age.toInt() < MIN_AGE) {
                builder.setMessage(R.string.error_profile_underage)
            } else if (inches.toInt() >= INCHES_IN_FEET) {
                builder.setMessage(R.string.error_profile_invalid_height_inches)
            } else {
                val bmi = calculateBMI(feet.toInt(), inches.toInt(), pounds.toInt())
                if (bmi < MIN_BMI || bmi > MAX_BMI) {
                    builder.setMessage(R.string.error_profile_invalid_bmi)
                } else {
                    builder.setMessage(R.string.success_profile_updated)
                    // actually insert into the db and things
                }
            }
            builder.show()
        }

        // Set edit diet restrictions behavior
        val dietButton = root.findViewById<Button>(R.id.diet_button)
        dietButton.setOnClickListener{
            // redirect to new screen
        }

        return root
    }
}

private class HintAdapter(context: Context?, theLayoutResId: Int, objects: List<Any>)
            : ArrayAdapter<Any>(context, theLayoutResId, objects) {
    override fun getCount(): Int {
        val count = super.getCount()
        return if (count > 0) count - 1 else count
    }
}