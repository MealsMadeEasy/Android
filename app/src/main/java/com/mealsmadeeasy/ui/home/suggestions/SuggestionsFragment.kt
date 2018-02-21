package com.mealsmadeeasy.ui.home.suggestions

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.ui.BaseFragment
import com.mealsmadeeasy.ui.meal.MealActivity
import javax.inject.Inject

class SuggestionsFragment : BaseFragment() {

    @Inject lateinit var mealStore: MealStore

    companion object {
        private const val TAG = "SuggestionsFragment"
        fun newInstance(): SuggestionsFragment = SuggestionsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_suggestions, container, false)
        val list = root.findViewById<RecyclerView>(R.id.suggestion_list)
        mealStore.getSuggestedMeals().subscribe({ suggestions ->
            list.adapter = SuggestionsAdapter(suggestions) { _, mealId ->
                startActivity(MealActivity.newIntent(context, mealId))
            }
            list.layoutManager = LinearLayoutManager(root.context)
        }, { throwable ->
            Log.e(TAG, "Failed to load suggestions", throwable)
            Snackbar.make(root, R.string.failed_to_load_suggestions, Snackbar.LENGTH_LONG).show()
        })
        return root
    }

}
