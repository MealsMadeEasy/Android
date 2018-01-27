package com.mealsmadeeasy.ui.home.profile

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mealsmadeeasy.R
import com.mealsmadeeasy.ui.BaseFragment

class DietaryRestrictionsFragment : BaseFragment() {
    companion object {
        fun newInstance() = DietaryRestrictionsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.single_recycler_view, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.grocery_list_recycler_view)
        val adapter = DietaryRestrictionsAdapter()
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)

        return root
    }

}