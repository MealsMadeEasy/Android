package com.mealsmadeeasy.ui.home.glance

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.MealPeriod
import com.mealsmadeeasy.model.MealPortion
import org.joda.time.DateTime
import javax.inject.Inject

private const val KEY_SAVED_MEALPORTION = "EditServingsFragment.MEALPORTION"
private const val KEY_SAVED_MEALPERIOD = "EditServingsFragment.MEALPERIOD"
private const val KEY_SAVED_DATETIME = "EditServingsFragment.DATETIME"
private const val KEY_SAVED_NUMSERVINGS = "EditServingsFragment.NUMSERVINGS"

class EditServingsFragment : DialogFragment() {
    @Inject lateinit var mealStore: MealStore
    private lateinit var mealPortion: MealPortion
    private lateinit var mealPeriod: MealPeriod
    private lateinit var date: DateTime
    private var numServings = -1

    companion object {
        fun newInstance(mealPortion: MealPortion, mealPeriod: MealPeriod, date: DateTime): EditServingsFragment {
            val frag = EditServingsFragment()
            frag.arguments = Bundle().apply {
                putParcelable(KEY_SAVED_MEALPORTION, mealPortion)
                putInt(KEY_SAVED_MEALPERIOD, mealPeriod.ordinal)
                putLong(KEY_SAVED_DATETIME, date.millis)
            }
            return frag
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_SAVED_NUMSERVINGS, numServings)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val savedMealPortion = arguments!!.getParcelable<MealPortion>(KEY_SAVED_MEALPORTION)
        mealPortion = MealPortion(savedMealPortion.meal, savedMealPortion.servings)
        mealPeriod = MealPeriod.values()[arguments!!.getInt(KEY_SAVED_MEALPERIOD)]
        date = DateTime(arguments!!.getLong(KEY_SAVED_DATETIME))

        val title = arguments?.getParcelable<MealPortion>(KEY_SAVED_MEALPORTION)?.meal?.name
        numServings = savedInstanceState?.getInt(KEY_SAVED_NUMSERVINGS) ?:
                arguments!!.getParcelable<MealPortion>(KEY_SAVED_MEALPORTION).servings

        val view = View.inflate(context, R.layout.view_edit_servings, null)
        setPlusAndMinusButtonListeners(view)
        return AlertDialog.Builder(activity)
                .setView(view)
                .setTitle(context!!.resources.getString(R.string.week_at_a_glance_edit_servings_title, title))
                .setPositiveButton(R.string.week_at_a_glance_edit_servings_confirm) { _, _ ->
                        mealStore.removeMealFromMealPlan(mealPortion.meal, date, mealPeriod)
                        mealStore.addMealToMealPlan(mealPortion.meal, date, mealPeriod, numServings)
                    Snackbar.make(activity!!.findViewById(R.id.week_at_a_glance_recycler_view),
                            R.string.week_at_a_glance_edit_servings_updated, Snackbar.LENGTH_SHORT)
                            .show()
                }
                .setNegativeButton(R.string.week_at_a_glance_edit_servings_cancel) { _, _ ->
                    // Do nothing.
                }
                .create()
    }

    private fun setPlusAndMinusButtonListeners(view: View) {
        val servingsText = view.findViewById<TextView>(R.id.add_to_plan_servings)
        val plusButton = view.findViewById<ImageView>(R.id.add_to_plan_plus)
        val minusButton = view.findViewById<ImageView>(R.id.add_to_plan_minus)

        servingsText.text = numServings.toString()
        minusButton.isEnabled = numServings != 1

        minusButton.setOnClickListener {
            if (numServings != 1) {
                numServings--
                servingsText.text = numServings.toString()
                minusButton.isEnabled = (numServings > 1)
            }
        }

        plusButton.setOnClickListener {
            numServings++
            minusButton.isEnabled = (numServings > 1)
            servingsText.text = numServings.toString()
        }
    }
}