package com.mealsmadeeasy.ui.home.glance

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.ui.BaseFragment
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class WeekAtAGlanceFragment : BaseFragment() {

    @Inject lateinit var mealStore: MealStore

    companion object {
        fun newInstance(): WeekAtAGlanceFragment = WeekAtAGlanceFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_week_at_a_glance, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.week_at_a_glance_recycler_view)
        rv.layoutManager = LinearLayoutManager(context)

        mealStore.getMealPlan()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .subscribe({ mealPlan ->
                    (rv.adapter as? WeekAtAGlanceAdapter).let {
                        if (it == null) {
                            rv.adapter = WeekAtAGlanceAdapter(mealPlan = mealPlan) { meal, mealPeriod, date ->
                                mealStore.removeMealFromMealPlan(meal, date, mealPeriod)
                                Snackbar.make(root.findViewById<RecyclerView>(R.id.week_at_a_glance_recycler_view),
                                        R.string.week_at_a_glance_meal_deleted, Snackbar.LENGTH_SHORT)
                                        .setAction(R.string.week_at_a_glance_undo_delete) {
                                            mealStore.addMealToMealPlan(meal, date, mealPeriod)
                                        }
                                        .show()
                            }
                        } else {
                            it.mealPlan = mealPlan
                        }
                    }
                })

        return root
    }

}