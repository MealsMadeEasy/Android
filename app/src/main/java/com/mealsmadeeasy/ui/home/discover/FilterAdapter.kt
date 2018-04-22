package com.mealsmadeeasy.ui.home.discover

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Filter

class FilterAdapter(
        data: List<Filter> = emptyList(),
        selectedCategories: Collection<Filter> = emptySet())
    : RecyclerView.Adapter<FilterViewHolder>() {

    var data: List<Filter> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val selected = selectedCategories.toMutableSet()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_filter_category, parent, false)
        return FilterViewHolder(inflater) { category, include ->
            if (include) {
                selected += category
            } else {
                selected -= category
            }
        }
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        val category = data[position]
        holder.bind(category, category in selected)
    }
}

class FilterViewHolder(
        root: View,
        private val onSelectionChange: (Filter, Boolean) -> Unit)
    : RecyclerView.ViewHolder(root) {

    private lateinit var filter : Filter
    private val name = root.findViewById<CheckedTextView>(R.id.filter_name)

    init {
        name.setOnClickListener {
            name.isChecked = !name.isChecked
            onSelectionChange(filter, name.isChecked)
        }
    }

    fun bind(filter: Filter, isChecked: Boolean) {
        this.filter = filter
        name.text = filter.name
        name.isChecked = isChecked
    }
}

