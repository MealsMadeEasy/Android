package com.mealsmadeeasy.ui.home.discover

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Filter
import com.mealsmadeeasy.model.FilterGroup

class FilterGroupAdapter(
        groups: List<FilterGroup> = emptyList(),
        selected: Set<Filter> = emptySet()
) : RecyclerView.Adapter<FilterGroupAdapter.FilterGroupViewHolder>() {

    var groups: List<FilterGroup> = groups
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    val selected = selected.toMutableSet()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterGroupViewHolder {
        val root = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_filter_group, parent, false)

        return FilterGroupViewHolder(root)
    }

    override fun getItemCount() = groups.size

    override fun onBindViewHolder(holder: FilterGroupViewHolder, position: Int) {
        holder.bind(groups[position])
    }

    inner class FilterGroupViewHolder(root: View) : RecyclerView.ViewHolder(root) {

        private val name = root.findViewById<TextView>(R.id.filter_group_title)
        private val adapter: FilterAdapter

        init {
            val recyclerView = root.findViewById<RecyclerView>(R.id.filter_group_content_adapter)
            recyclerView.layoutManager = LinearLayoutManager(root.context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }

            adapter = FilterAdapter(selected = selected).also {
                recyclerView.adapter = it
            }
        }

        fun bind(group: FilterGroup) {
            adapter.group = group
            adapter.data = group.filters
            name.text = group.groupName
        }

    }

}
