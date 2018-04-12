package com.mealsmadeeasy.ui.home.discover

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import javax.inject.Inject

class FilterBottomDialogFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var mealStore: MealStore

    companion object {
        const val TAG = "FilterBDSFragment"

        fun newInstance(): FilterBottomDialogFragment {
            return FilterBottomDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_filter, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.filter_recycler_view)
        val layoutManager = LinearLayoutManager(root.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
//        layoutManager.stackFromEnd = true
        mealStore.getAvailableFilters()
                .subscribe({ filterGroup ->
                    rv.adapter = FilterAdapter(filterGroup[0].filters)
                    rv.layoutManager = layoutManager
                }, { throwable ->
                    Log.e(TAG, "Failed to load filters", throwable)
                })
        return root
    }
}