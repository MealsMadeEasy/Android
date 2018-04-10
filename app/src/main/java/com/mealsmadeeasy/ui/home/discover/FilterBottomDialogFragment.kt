package com.mealsmadeeasy.ui.home.discover

import android.support.design.widget.BottomSheetDialogFragment

class FilterBottomDialogFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "FilterBottomDialogFragment"

        fun newInstance(): FilterBottomDialogFragment {
            return FilterBottomDialogFragment()
        }
    }


}