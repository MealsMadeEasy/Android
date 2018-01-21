package com.mealsmadeeasy.ui.home.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Sex
import com.mealsmadeeasy.ui.BaseFragment
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText

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

        fun calculateBMI(feet: Int, inches: Int, pounds: Double): Double {
            return poundsToKilograms(pounds) / heightToMeters(feet, inches)
        }

        fun heightToMeters(feet: Int, inches: Int): Double {
            return feet * FEET_TO_METERS + inches * INCHES_TO_METERS
        }

        fun poundsToKilograms(pounds: Double): Double {
            return pounds * POUNDS_TO_KILOGGRAMS
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.view_user_profile, container, false)

        // Populate spinner values
        val sexSpinner = root.findViewById<Spinner>(R.id.sex_spinner)
        val sexValues: List<String> = listOf(
                Sex.MALE.toString(), Sex.FEMALE.toString(), Sex.OTHER.toString())
        val sexAdapter = ArrayAdapter<String>(this.activity, android.R.layout.simple_list_item_1, sexValues)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = sexAdapter

        // Set button behavior
        val button = root.findViewById<Button>(R.id.save_button)
        button.setOnClickListener{
            val sex = sexSpinner.selectedItem
            val age = root.findViewById<EditText>(R.id.age_field).text.toString().toInt()
            val feet = root.findViewById<EditText>(R.id.height_feet_field).text.toString().toInt()
            val inches = root.findViewById<EditText>(R.id.height_inches_field).text.toString().toInt()
            val pounds = root.findViewById<EditText>(R.id.weight_field).toString().toDouble()
            val bmi = calculateBMI(feet, inches, pounds)

            if (age < MIN_AGE) {
                // alert
            } else if (inches >= INCHES_IN_FEET) {
                // alert
            } else if (bmi < MIN_BMI || bmi > MAX_BMI) {
                // bmi alert
            } else {
                // update DB
            }
        }

        return root
    }
}