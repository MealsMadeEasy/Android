package com.mealsmadeeasy.ui.home.discover

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.Filter
import javax.inject.Inject

private const val KEY_SAVED_SEL_FILTERS = "FilterFrag.SELECTED_FILTERS"

class FilterBottomDialogFragment : BottomSheetDialogFragment() {
    @Inject
    lateinit var mealStore: MealStore

    private lateinit var selectedCategories: Set<Filter>
    private lateinit var filterAdapter: FilterAdapter

    companion object {
        const val TAG = "FilterFragment"
        const val RETURN_FILTER = "FilterFragment"

        fun newInstance(): FilterBottomDialogFragment {
            return FilterBottomDialogFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(KEY_SAVED_SEL_FILTERS, ArrayList<Filter>(filterAdapter.selected.toList()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (savedInstanceState != null) {
            selectedCategories = savedInstanceState.getParcelableArrayList<Filter>(KEY_SAVED_SEL_FILTERS).toSet()
        } else {
            filterAdapter = FilterAdapter()
            selectedCategories = filterAdapter.selected
        }

        val root = inflater.inflate(R.layout.fragment_filter, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.filter_recycler_view)
        val layoutManager = LinearLayoutManager(root.context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rv.layoutManager = layoutManager
        filterAdapter = FilterAdapter(selectedCategories = selectedCategories)

        mealStore.getAvailableFilters()
                .subscribe({ filterGroup ->
                    filterAdapter.data = filterGroup.flatMap {
                        it.filters
                    }
                    rv.adapter = filterAdapter
                }, { throwable ->
                    Log.e(TAG, "Failed to load filters", throwable)
                })

        val clearButton = root.findViewById<Button>(R.id.filter_clear_filters)
        clearButton.setOnClickListener {
            selectedCategories = emptySet()
            dismiss()
        }

        return root
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        val data = Intent().apply {
            putParcelableArrayListExtra(RETURN_FILTER, ArrayList(filterAdapter.selected))
        }
        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, data)
    }
}