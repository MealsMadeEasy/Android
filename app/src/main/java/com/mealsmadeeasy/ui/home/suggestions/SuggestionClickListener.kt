package com.mealsmadeeasy.ui.home.suggestions

import android.view.View

interface SuggestionClickListener {
    fun suggestionClicked(view: View, id: String)
}