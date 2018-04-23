package com.mealsmadeeasy.ui.home.discover

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Filter
import com.mealsmadeeasy.model.FilterGroup

class FilterAdapter(
        data: List<Filter> = emptyList(),
        private val selected: MutableSet<Filter>
) : RecyclerView.Adapter<FilterViewHolder>() {

    lateinit var group: FilterGroup

    var data: List<Filter> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_filter_category, parent, false)
        return FilterViewHolder(inflater) { category, include ->
            if (include) {
                val active = selected.filter { it in group.filters }
                if (active.size >= group.maximumActive) {
                    selected -= active.first()
                    notifyDataSetChanged()
                }

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

