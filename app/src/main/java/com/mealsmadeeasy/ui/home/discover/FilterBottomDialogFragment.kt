package com.mealsmadeeasy.ui.home.discover

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mealsmadeeasy.R

class FilterBottomDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "FilterBottomDialogFragment"

        fun newInstance(): FilterBottomDialogFragment {
            return FilterBottomDialogFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_filter, container, false)
        return root
    }


}