package com.mealsmadeeasy.ui.home.grocery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Ingredient

class GroceryListAdapter(data: List<Ingredient> = emptyList()) : RecyclerView.Adapter<GroceryListViewHolder>() {

    var data: List<Ingredient> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return GroceryListViewHolder(inflater.inflate(R.layout.view_grocery_list, parent, false))
    }

    override fun onBindViewHolder(holder: GroceryListViewHolder, position: Int) {
        holder.bind(data[position])
    }
}

class GroceryListViewHolder(root: View) : RecyclerView.ViewHolder(root) {

    private val quantity = root.findViewById<TextView>(R.id.quantity)
    private val name = root.findViewById<TextView>(R.id.ingredient_name)

    fun bind(ingredient: Ingredient) {
        name.text = ingredient.name
        quantity.text = ingredient.quantity.toString()
    }
}

