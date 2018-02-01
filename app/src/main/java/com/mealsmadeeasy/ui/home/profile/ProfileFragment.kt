package com.mealsmadeeasy.ui.home.profile

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
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


private const val MIN_AGE = 13
private const val INCHES_IN_FEET = 12
private const val MIN_BMI = 15
private const val MAX_BMI = 50

class ProfileFragment : BaseFragment() {
    private lateinit var sexSpinner: Spinner
    private lateinit var bdayPicker: EditText
    private lateinit var feetText: EditText
    private lateinit var inchesText: EditText
    private lateinit var weightText: EditText
    private lateinit var submitButton: Button
    private var cal = Calendar.getInstance()

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.view_user_profile, container, false)

        sexSpinner = root.findViewById(R.id.sex_spinner)
        bdayPicker = root.findViewById(R.id.birthday_edit_text)
        feetText = root.findViewById(R.id.height_feet_edit_text)
        inchesText = root.findViewById(R.id.height_inches_edit_text)
        weightText = root.findViewById(R.id.weight_field)
        submitButton = root.findViewById(R.id.save_button)

        // Populate sex spinner values
        var sexValues: List<String> = emptyList()
        for (item in Gender.valuesAsStringRes()) {
            sexValues += getString(item)
        }
        sexValues += getString(R.string.profile_spinner_prompt)
        val sexAdapter = HintAdapter(this.activity, android.R.layout.simple_list_item_1, sexValues)
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = sexAdapter
        sexSpinner.setSelection(sexAdapter.count)

        // Set up birthday date picker
        val dateText = root.findViewById<EditText>(R.id.birthday_edit_text)
        setupDatePicker(dateText)

        // Set up text input view hints
        val feetContainer = root.findViewById<TextInputLayout>(R.id.height_feet_container)
        val inchesContainer = root.findViewById<TextInputLayout>(R.id.height_inches_container)
        val weightContainer = root.findViewById<TextInputLayout>(R.id.weight_container)

        feetText.onFocusChangeListener = TextInputViewHintListener(feetContainer,
                getString(R.string.profile_height_prompt), getString(R.string.profile_height_feet_units))
        inchesText.onFocusChangeListener = TextInputViewHintListener(inchesContainer,
                getString(R.string.profile_height_inches_prompt), getString(R.string.profile_height_inches_units))
        weightText.onFocusChangeListener = TextInputViewHintListener(weightContainer,
                getString(R.string.profile_weight_prompt), getString(R.string.profile_weight_lbs_units))

        // Enable button iff all fields are empty
        val editTextList = listOf(bdayPicker, feetText, inchesText, weightText)
        sexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                submitButton.isEnabled = areAllFieldsEnabled()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing.
            }
        }

        val watcher = ButtonTextWatcher()
        for (editText in editTextList) {
            editText.addTextChangedListener(watcher)
        }

        onSubmitClicked()

        return root
    }

    private fun setupDatePicker(editText: EditText) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            editText.setText(DateFormat.getDateFormat(context).format(Date(cal.timeInMillis)))

        }

        editText.setOnClickListener {
            DatePickerDialog(context, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun onSubmitClicked() {
        submitButton.setOnClickListener{
            val sex = sexSpinner.selectedItemPosition
            val age = cal.timeInMillis
            val feet = feetText.text.toString()
            val inches = inchesText.text.toString()
            val pounds = weightText.text.toString()

            val builder = AlertDialog.Builder(this.activity)
            builder.setPositiveButton(getString(R.string.profile_prompt_positive_ack),  {_, _ ->
                // Do nothing.
            })
            val user = UserProfile(Gender.values()[sex], age,
                    heightToInches(feet.toInt(), inches.toInt()), pounds.toInt())
            if (calculateAge() < MIN_AGE) {
                builder.setMessage(R.string.error_profile_underage)
            } else if (inches.toInt() >= INCHES_IN_FEET) {
                builder.setMessage(R.string.error_profile_invalid_height_inches)
            } else {
                if (user.bmi < MIN_BMI || user.bmi > MAX_BMI) {
                    builder.setMessage(R.string.error_profile_invalid_bmi)
                } else {
                    builder.setMessage(R.string.success_profile_updated)
                    insertIntoDatabase(user)
                }
            }
            builder.show()
        }
    }

    private fun insertIntoDatabase(user: UserProfile) {
        // TODO
    }

    private fun heightToInches(feet: Int, inches: Int): Int {
        return feet * INCHES_IN_FEET + inches
    }

    private fun calculateAge(): Int {
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }

    private fun areAllFieldsEnabled(): Boolean {
        val spinner = sexSpinner.selectedItem
        val birthday = bdayPicker.text
        val heightFeet = feetText.text
        val heightInches = inchesText.text
        val weight = weightText.text

        return spinner != getString(R.string.profile_spinner_prompt)
                && !birthday.isBlank() && !heightFeet.isBlank() && !heightInches.isBlank()
                && !weight.isBlank()
    }

    private inner class ButtonTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            submitButton.isEnabled = areAllFieldsEnabled()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Do nothing.
        }
    }
}

private class TextInputViewHintListener(val container: TextInputLayout, val dummyText: String,
                                        val hint: String) : View.OnFocusChangeListener {
    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (hasFocus) {
            container.hint = hint
        } else {
            if (container.editText!!.text.isBlank()) {
                container.hint = dummyText
            }
        }
    }
}

private class HintAdapter<T>(context: Context?, theLayoutResId: Int, objects: List<T>)
            : ArrayAdapter<T>(context, theLayoutResId, objects) {
    override fun getCount(): Int {
        val count = super.getCount()
        return if (count > 0) count - 1 else count
    }
}