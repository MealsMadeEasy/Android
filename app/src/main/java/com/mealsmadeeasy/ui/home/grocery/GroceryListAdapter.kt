package com.mealsmadeeasy.ui.home.grocery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Ingredient

class GroceryListAdapter(data: List<Ingredient> = emptyList()) : RecyclerView.Adapter<GroceryListViewHolder>() {

    var data: List<Ingredient> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    val checkboxMap = mutableMapOf<Ingredient, Boolean>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GroceryListViewHolder(inflater.inflate(R.layout.view_grocery_list, parent, false),
                        checkboxMap)
    }

    override fun onBindViewHolder(holder: GroceryListViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

class GroceryListViewHolder(root: View, val checkboxMap : MutableMap<Ingredient, Boolean>)
            : RecyclerView.ViewHolder(root) {

    private lateinit var ingredient : Ingredient
    private val quantity = root.findViewById<TextView>(R.id.ingredient_quantity)
    private val name = root.findViewById<TextView>(R.id.ingredient_name)
    private val unit = root.findViewById<TextView>(R.id.ingredient_unit)
    private val checkbox = root.findViewById<CheckBox>(R.id.ingredient_checkbox)

    init {
        checkbox.setOnCheckedChangeListener { _, checked ->
            checkboxMap[ingredient] = checked
        }
        root.findViewById<View>(R.id.grocery_list_container).setOnClickListener { _ ->
            checkbox.isChecked = !checkbox.isChecked
        }
    }

    fun bind(ingredient: Ingredient) {
        this.ingredient = ingredient
        name.text = ingredient.name
        quantity.text = ingredient.quantity.toString()
        unit.text = ingredient.unitName
        checkbox.isChecked = checkboxMap[ingredient] ?: false
    }
}

