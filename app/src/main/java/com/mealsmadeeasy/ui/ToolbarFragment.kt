package com.mealsmadeeasy.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mealsmadeeasy.R

abstract class ToolbarFragment : BaseFragment() {

    abstract val title: String
    abstract val canNavigateUp: Boolean

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                    savedInstanceState: Bundle?): View? {

        val root = inflater.inflate(R.layout.fragment_toolbar, container)
        val contentView = root.findViewById<ViewGroup>(R.id.toolbar_fragment_content_view)
        val toolbar = root.findViewById<Toolbar>(R.id.toolbar)

        contentView.addView(onCreateContentView(inflater, contentView, savedInstanceState))

        toolbar.title = title
        (activity as? AppCompatActivity)?.let { activity ->
            activity.setSupportActionBar(toolbar)

            activity.supportActionBar?.let { toolbar ->
                toolbar.setDisplayHomeAsUpEnabled(canNavigateUp)
                toolbar.setDisplayShowHomeEnabled(canNavigateUp)
            }
        }

        return root
    }

    abstract fun onCreateContentView(inflater: LayoutInflater, container: ViewGroup,
                                     savedInstanceState: Bundle?): View

}