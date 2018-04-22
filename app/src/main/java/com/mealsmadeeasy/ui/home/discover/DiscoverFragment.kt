package com.mealsmadeeasy.ui.home.discover

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.ui.BaseFragment
import com.mealsmadeeasy.ui.meal.MealActivity
import javax.inject.Inject

class DiscoverFragment : BaseFragment() {

    @Inject lateinit var mealStore: MealStore

    companion object {
        const val TAG = "DiscoverFragment"
        fun newInstance(): DiscoverFragment = DiscoverFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_discover, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_search -> {
                startActivity(SearchActivity.newIntent(context!!))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_discover, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.suggestion_list)
        mealStore.getSuggestedMeals().subscribe({ suggestions ->
            recyclerView.adapter = SuggestionsAdapter(suggestions) { _, mealId ->
                startActivity(MealActivity.newIntent(context, mealId))
            }
            recyclerView.layoutManager = LinearLayoutManager(root.context)
        }, { throwable ->
            Log.e(TAG, "Failed to load suggestions", throwable)
            Snackbar.make(root, R.string.failed_to_load_suggestions, Snackbar.LENGTH_LONG).show()
        })
        return root
    }

}
