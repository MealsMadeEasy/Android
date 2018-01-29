package com.mealsmadeeasy.ui.home.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mealsmadeeasy.R
import com.mealsmadeeasy.ui.BaseFragment

import java.util.*
import android.widget.ArrayAdapter
import com.mealsmadeeasy.model.Gender
import com.mealsmadeeasy.model.UserProfile
import java.text.SimpleDateFormat


const val MIN_AGE = 13
const val INCHES_IN_FEET = 12
const val MIN_BMI = 15
const val MAX_BMI = 50

class ProfileFragment : BaseFragment() {

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()

        fun heightToInches(feet: Int, inches: Int): Int {
            return feet * INCHES_IN_FEET + inches
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.view_user_profile, container, false)

        // Populate sex spinner values
        val sexSpinner = root.findViewById<Spinner>(R.id.sex_spinner)
        var sexValues: List<String> = emptyList()
        for (item in Gender.values()) {
            sexValues += item.toString()
        }
        sexValues += getString(R.string.profile_spinner_prompt)
        val sexAdapter = HintAdapter(this.activity, android.R.layout.simple_list_item_1, sexValues)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = sexAdapter
        sexSpinner.setSelection(sexAdapter.count)

        // Set up birthday date picker
        val dateText = root.findViewById<EditText>(R.id.birthday_edit_text)
        val cal = Calendar.getInstance()
        setupDatePicker(dateText, cal)

        // Set up text input view hints
        val feetContainer = root.findViewById<TextInputLayout>(R.id.height_feet_container)
        val feetEditText = root.findViewById<TextInputEditText>(R.id.height_feet_edit_text)
        val inchesContainer = root.findViewById<TextInputLayout>(R.id.height_inches_container)
        val inchesEditText = root.findViewById<TextInputEditText>(R.id.height_inches_edit_text)
        val weightContainer = root.findViewById<TextInputLayout>(R.id.weight_container)
        val weightEditText = root.findViewById<TextInputEditText>(R.id.weight_field)
        feetEditText.onFocusChangeListener = TextInputViewHintListener(feetContainer,
                getString(R.string.profile_height_prompt), getString(R.string.profile_height_feet_units))
        inchesEditText.onFocusChangeListener = TextInputViewHintListener(inchesContainer,
                getString(R.string.profile_height_inches_prompt), getString(R.string.profile_height_inches_units))
        weightEditText.onFocusChangeListener = TextInputViewHintListener(weightContainer,
                getString(R.string.profile_weight_prompt), getString(R.string.profile_weight_lbs_units))

        // Set save button behavior
        val saveButton = root.findViewById<Button>(R.id.save_button)
        saveButton.setOnClickListener{
            val sex = sexSpinner.selectedItem
            val age = cal.timeInMillis
            val feet = feetEditText.text.toString()
            val inches = inchesEditText.text.toString()
            val pounds = weightEditText.text.toString()

            val builder = AlertDialog.Builder(this.activity)
            if (sex == getString(R.string.profile_spinner_prompt)
                    || dateText.text.toString() == ""
                    || feet == "" || inches == "" || pounds == "") {
                builder.setMessage(R.string.error_profile_blank_field)
            } else if (age.toInt() < MIN_AGE) {
                builder.setMessage(R.string.error_profile_underage)
            } else if (inches.toInt() >= INCHES_IN_FEET) {
                builder.setMessage(R.string.error_profile_invalid_height_inches)
            } else {
                val bmi = UserProfile.calculateBMI(feet.toInt(), inches.toInt(), pounds.toInt())
                if (bmi < MIN_BMI || bmi > MAX_BMI) {
                    builder.setMessage(R.string.error_profile_invalid_bmi)
                } else {
                    builder.setMessage(R.string.success_profile_updated)
                    val user = UserProfile(Gender.getByString(sex.toString()), age,
                            heightToInches(feet.toInt(), inches.toInt()), pounds.toInt())
                    insertIntoDatabase(user)
                }
            }
            builder.show()
        }

        // Set edit diet restrictions behavior
//        val dietButton = root.findViewById<Button>(R.id.diet_button)
//        dietButton.setOnClickListener{
            // redirect to new screen
//        }

        return root
    }

    private fun setupDatePicker(editText: EditText, cal: Calendar) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            editText.setText(SimpleDateFormat("dd.MM.yyyy").format(cal.time))

        }

        editText.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun insertIntoDatabase(user: UserProfile) {
        // TODO
    }
}

private class HintAdapter<T>(context: Context?, theLayoutResId: Int, objects: List<T>)
            : ArrayAdapter<T>(context, theLayoutResId, objects) {
    override fun getCount(): Int {
        val count = super.getCount()
        return if (count > 0) count - 1 else count
    }
}

private class TextInputViewHintListener(val container: TextInputLayout, val dummyText: String, val hint: String)
            : View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            container.hint = hint
        } else {
            container.hint = dummyText
        }
    }
}