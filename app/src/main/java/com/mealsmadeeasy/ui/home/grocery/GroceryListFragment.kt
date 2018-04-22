package com.mealsmadeeasy.ui.home.grocery

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
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class GroceryListFragment : BaseFragment() {

    @Inject lateinit var mealStore: MealStore

    companion object {
        private const val TAG = "GroceryListFragment"
        fun newInstance(): GroceryListFragment = GroceryListFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.single_recycler_view, container, false)
        val rv = root.findViewById<RecyclerView>(R.id.grocery_list_recycler_view)
        val adapter = GroceryListAdapter(mealStore = mealStore)
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        mealStore.getIngredientsForMealPlan()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .bindToLifecycle(this)
                .subscribe({ ingredients ->
                    adapter.data = ingredients
                }, { throwable ->
                    Log.e(TAG, "Failed to load grocery list", throwable)
                    Snackbar.make(root, R.string.error_grocery_list_load, Snackbar.LENGTH_LONG).show()
                })

        return root
    }

}