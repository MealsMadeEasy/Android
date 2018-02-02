package com.mealsmadeeasy.ui.home.profile

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.UserManager
import com.mealsmadeeasy.model.Gender
import com.mealsmadeeasy.model.UserProfile
import com.mealsmadeeasy.ui.BaseFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import java.util.*
import javax.inject.Inject

private const val TAG = "ProfileFragment"

private const val MIN_AGE = 13
private const val INCHES_IN_FEET = 12
private const val MIN_BMI = 15
private const val MAX_BMI = 50

class ProfileFragment : BaseFragment() {

    @Inject lateinit var userManager: UserManager

    private lateinit var loadingSpinner: ProgressBar
    private lateinit var savingSpinner: ProgressBar

    private lateinit var sexSpinner: Spinner
    private lateinit var bdayPicker: TextView
    private lateinit var feetText: EditText
    private lateinit var inchesText: EditText
    private lateinit var weightText: EditText
    private lateinit var submitButton: Button
    private var cal = Calendar.getInstance()

    companion object {
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.view_user_profile, container, false)
        root.findViewById<LinearLayout>(R.id.user_profile_container).requestFocus()

        loadingSpinner = root.findViewById(R.id.profile_loading_spinner)
        savingSpinner = root.findViewById(R.id.profile_saving_spinner)

        sexSpinner = root.findViewById(R.id.sex_spinner)
        bdayPicker = root.findViewById(R.id.birthday_text_view)
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
        setupDatePicker(bdayPicker)

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
        loadData()

        return root
    }

    private fun loadData() {
        userManager.isUserOnboarded().bindToLifecycle(this)
                .subscribe({ onboarded ->
                    if (onboarded) {
                        populateFormWithServerData()
                    } else {
                        showForm()
                    }
                })
    }

    private fun populateFormWithServerData() {
        userManager.getUserProfile()
                .bindToLifecycle(this)
                .subscribe({ userProfile ->
                    sexSpinner.setSelection(Gender.values().indexOf(userProfile.gender))
                    feetText.setText((userProfile.height / INCHES_IN_FEET).toString())
                    inchesText.setText((userProfile.height % INCHES_IN_FEET).toString())
                    weightText.setText(userProfile.weight.toString())

                    cal.timeInMillis = userProfile.birthday
                    bdayPicker.setText(DateFormat.getDateFormat(context).format(Date(cal.timeInMillis)))
                    showForm()
                }, { throwable ->
                    Log.e(TAG, "Failed to read user profile")
                    AlertDialog.Builder(context!!)
                            .setMessage(R.string.error_profile_not_loaded)
                            .setPositiveButton(R.string.action_try_again) { _, _ ->
                                populateFormWithServerData()
                            }
                            .setNegativeButton(R.string.action_cancel) { _, _ ->
                                activity?.finish()
                            }
                            .show()
                })
    }

    private fun showForm() {
        loadingSpinner.visibility = View.GONE
    }

    private fun setupDatePicker(textView: TextView) {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            textView.text = DateFormat.getDateFormat(context).format(Date(cal.timeInMillis))

        }

        textView.setOnClickListener {
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

            val hasError: Boolean
            val builder = AlertDialog.Builder(context!!)
            builder.setPositiveButton(getString(R.string.profile_prompt_positive_ack),  {_, _ ->
                // Do nothing.
            })
            val user = UserProfile(Gender.values()[sex], age,
                    heightToInches(feet.toInt(), inches.toInt()), pounds.toInt())
            when {
                calculateAge() < MIN_AGE -> {
                    builder.setMessage(R.string.error_profile_underage)
                    hasError = true
                }
                inches.toInt() >= INCHES_IN_FEET -> {
                    builder.setMessage(R.string.error_profile_invalid_height_inches)
                    hasError = true
                }
                user.bmi < MIN_BMI || user.bmi > MAX_BMI -> {
                    builder.setMessage(R.string.error_profile_invalid_bmi)
                    hasError = true
                }
                else -> {
                    insertIntoDatabase(user)
                    hasError = false
                }
            }

            if (hasError) {
                builder.show()
            }
        }
    }

    private fun insertIntoDatabase(user: UserProfile) {
        setSaveInProgress(true)
        userManager.updateUserProfile(user)
                .bindToLifecycle(this)
                .subscribe({ successful ->
                    if (successful) {
                        showSnackbar(R.string.success_profile_updated)
                    } else {
                        showSnackbar(R.string.error_profile_not_updated)
                    }
                    setSaveInProgress(false)
                }, { throwable ->
                    Log.e(TAG, "Failed to update profile", throwable)
                    showSnackbar(R.string.error_profile_not_updated)
                    setSaveInProgress(false)
                })
    }

    private fun setSaveInProgress(saving: Boolean) {
        savingSpinner.visibility = if (saving) View.VISIBLE else View.GONE
        submitButton.visibility = if (saving) View.GONE else View.VISIBLE

        sexSpinner.isEnabled = !saving
        bdayPicker.isEnabled = !saving
        feetText.isEnabled = !saving
        inchesText.isEnabled = !saving
        weightText.isEnabled = !saving
    }

    private fun showSnackbar(@StringRes message: Int) {
        Snackbar.make(submitButton, message, Snackbar.LENGTH_LONG).show()
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