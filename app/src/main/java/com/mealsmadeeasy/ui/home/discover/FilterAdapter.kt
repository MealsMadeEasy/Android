package com.mealsmadeeasy.ui.home.discover

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Filter

class FilterAdapter(data: List<Filter> = emptyList()) : RecyclerView.Adapter<FilterViewHolder>() {

    var data: List<Filter> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FilterViewHolder(inflater.inflate(R.layout.view_filter_category, parent, false))
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

class FilterViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private lateinit var filter : Filter
    private val name = root.findViewById<TextView>(R.id.filter_name)

    fun bind(filter: Filter) {
        this.filter = filter
        name.text = filter.name
    }
}

