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
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class GroceryListAdapter(data: GroceryList = GroceryList(), val mealStore: MealStore) : RecyclerView.Adapter<GroceryListViewHolder>() {

    var data: GroceryList = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
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
    private val dependants = root.findViewById<TextView>(R.id.ingredient_dependents)
    private val unit = root.findViewById<TextView>(R.id.ingredient_unit)
    private val checkbox = root.findViewById<CheckBox>(R.id.ingredient_checkbox)

    init {
        checkbox.setOnCheckedChangeListener { _, checked ->
            if (checked != item.purchased) {
                mealStore.markIngredientPurchased(item.ingredient, checked)
            }
        }
        root.findViewById<View>(R.id.grocery_list_container).setOnClickListener { _ ->
            checkbox.isChecked = !checkbox.isChecked
        }
    }

    fun bind(entry: GroceryListEntry) {
        item = entry
        val ingredient = entry.ingredient

        val df = DecimalFormat("####.##")
        df.roundingMode = RoundingMode.HALF_UP
        val roundedQuantity = df.format(ingredient.quantity)

        name.text = ingredient.name
        dependants.text = item.dependants.joinToString(limit = 3)
        quantity.text = roundedQuantity.toString()
        unit.text = ingredient.unit
        checkbox.isChecked = entry.purchased
    }
}

