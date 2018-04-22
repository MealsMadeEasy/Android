package com.mealsmadeeasy.ui.home.grocery

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.model.GroceryList
import com.mealsmadeeasy.model.GroceryListEntry
import javax.inject.Inject

class GroceryListAdapter(data: GroceryList = GroceryList()) : RecyclerView.Adapter<GroceryListViewHolder>() {

    @Inject lateinit var mealStore: MealStore
    var data: GroceryList = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        MealsApplication.component(parent.context).inject(this)
        return GroceryListViewHolder(inflater.inflate(R.layout.view_grocery_list, parent, false), mealStore)
    }

    override fun onBindViewHolder(holder: GroceryListViewHolder, position: Int) {
        holder.bind(data.items[position])
    }
}

class GroceryListViewHolder(root: View, mealStore: MealStore) : RecyclerView.ViewHolder(root) {

    private lateinit var item : GroceryListEntry
    private val quantity = root.findViewById<TextView>(R.id.ingredient_quantity)
    private val name = root.findViewById<TextView>(R.id.ingredient_name)
    private val unit = root.findViewById<TextView>(R.id.ingredient_unit)
    private val checkbox = root.findViewById<CheckBox>(R.id.ingredient_checkbox)

    init {
        checkbox.setOnCheckedChangeListener { _, checked ->
            if (checked != item.purchased) {
                mealStore.markIngredientPurchased(item.ingredient,checked)
            }
        }
        root.findViewById<View>(R.id.grocery_list_container).setOnClickListener { _ ->
            checkbox.isChecked = !checkbox.isChecked
        }
    }

    fun bind(entry: GroceryListEntry) {
        item = entry

        val ingredient = entry.ingredient
        name.text = ingredient.name
        quantity.text = ingredient.quantity.toString()
        unit.text = ingredient.unit
        checkbox.isChecked = entry.purchased
    }
}

