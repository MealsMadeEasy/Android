package com.mealsmadeeasy.ui.home.profile

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.DietaryRestrictions

class DietaryRestrictionsAdapter(data: List<DietaryRestrictions> =
                                 DietaryRestrictions.values().toList()) :
                                    RecyclerView.Adapter<DietaryRestrictionsViewHolder>() {

    var data: List<DietaryRestrictions> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    val checkboxMap = mutableMapOf<DietaryRestrictions, Boolean>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DietaryRestrictionsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DietaryRestrictionsViewHolder(inflater.inflate(R.layout.view_user_profile_dietary_restrictions,
                parent, false), checkboxMap)
    }

    override fun onBindViewHolder(holder: DietaryRestrictionsViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

class DietaryRestrictionsViewHolder(root: View, val checkboxMap : MutableMap<DietaryRestrictions, Boolean>)
    : RecyclerView.ViewHolder(root) {

    private val restriction = root.findViewById<TextView>(R.id.dietary_restriction)
    private val checkbox = root.findViewById<CheckBox>(R.id.dietary_restriction_checkbox)

    init {
        checkbox.setOnCheckedChangeListener { _, checked ->
            checkboxMap[DietaryRestrictions.getByString(restriction.text.toString())] = checked
        }
        root.findViewById<View>(R.id.profile_dietary_restrictions_container).setOnClickListener { _ ->
            checkbox.isChecked = !checkbox.isChecked
        }
    }

    fun bind(restriction: DietaryRestrictions) {
        this.restriction.text = restriction.toString()
        checkbox.isChecked = checkboxMap[restriction] ?: false
    }

}

