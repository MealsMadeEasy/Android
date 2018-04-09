package com.mealsmadeeasy.ui.home.discover

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mealsmadeeasy.R
import com.mealsmadeeasy.model.Meal
import com.squareup.picasso.Picasso

class SuggestionsAdapter(
        meals: List<Meal> = emptyList(),
        private val suggestionClickListener: (View, String) -> Unit
) : RecyclerView.Adapter<SuggestionViewHolder>() {

    var meals = meals
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.view_suggestion, parent, false)
        return SuggestionViewHolder(layout, suggestionClickListener)
    }

    override fun getItemCount() = meals.size

    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        holder.bind(meals[position])
    }
}

class SuggestionViewHolder(
        view: View,
        private val suggestionClickListener: (View, String) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val thumbnailView = view.findViewById<ImageView>(R.id.suggestion_thumbnail)
    private val nameView = view.findViewById<TextView>(R.id.suggestion_name)
    private lateinit var meal: Meal

    init {
        view.setOnClickListener {
            suggestionClickListener(view, meal.id)
        }
    }

    fun bind(meal: Meal) {
        this.meal = meal
        nameView.text = meal.name
        Picasso.with(itemView.context)
                .load(meal.thumbnailUrl)
                .fit()
                .centerCrop()
                .into(thumbnailView)
    }
}