package com.mealsmadeeasy.ui.home.discover

import android.content.DialogInterface
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

private const val ARG_SEL_FILTERS = "FilterFrag.ARG_SELECTED_FILTERS"
private const val KEY_SAVED_SEL_FILTERS = "FilterFrag.SELECTED_FILTERS"

class FilterPickerDialogFragment : BottomSheetDialogFragment() {
    @Inject lateinit var mealStore: MealStore

    private lateinit var filterAdapter: FilterGroupAdapter

    companion object {
        const val TAG = "FilterFragment"

        fun newInstance(selected: List<Filter>): FilterPickerDialogFragment {
            return FilterPickerDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_SEL_FILTERS, ArrayList(selected))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelableArrayList(KEY_SAVED_SEL_FILTERS, ArrayList<Filter>(filterAdapter.selected))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val selectedCategories: Set<Filter>

        if (savedInstanceState != null) {
            selectedCategories = savedInstanceState.getParcelableArrayList<Filter>(KEY_SAVED_SEL_FILTERS).toSet()
        } else {
            selectedCategories = arguments?.getParcelableArrayList<Filter>(ARG_SEL_FILTERS)?.toSet().orEmpty()
        }

        val root = inflater.inflate(R.layout.fragment_filter, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.filter_recycler_view)
        val layoutManager = LinearLayoutManager(root.context)
        rv.layoutManager = layoutManager
        filterAdapter = FilterGroupAdapter(selected = selectedCategories)

        mealStore.getAvailableFilters()
                .subscribe({ filterGroup ->
                    filterAdapter.groups = filterGroup
                    rv.adapter = filterAdapter
                }, { throwable ->
                    Log.e(TAG, "Failed to load filters", throwable)
                })

        val clearButton = root.findViewById<Button>(R.id.filter_clear_filters)
        clearButton.setOnClickListener {
            filterAdapter.selected.clear()
            dismiss()
        }

        return root
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        (activity as SearchActivity?)?.setFilters(filterAdapter.selected.toList())
    }
}