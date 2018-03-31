package com.mealsmadeeasy.ui.home.discover

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.R
import com.mealsmadeeasy.data.MealStore
import com.mealsmadeeasy.ui.BaseActivity
import javax.inject.Inject
import android.support.v7.widget.SearchView
import android.util.Log
import android.widget.Toast
import com.mealsmadeeasy.ui.meal.MealActivity
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


class SearchActivity : BaseActivity() {

    @Inject lateinit var mealStore: MealStore
    private var querySubject : BehaviorSubject<String> = BehaviorSubject.create()

    companion object {
        private const val TAG = "SearchActivity"
        fun newIntent(context: Context?) = Intent(context, SearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        setContentView(R.layout.activity_search)

        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val searchResults = findViewById<RecyclerView>(R.id.search_results_list)

        getQueryObservable()
                .debounce(2, TimeUnit.SECONDS)
                .flatMapSingle { query ->
                    mealStore.search(query)
                }
                .subscribe({ results ->
                    searchResults.adapter = SuggestionsAdapter(results) { _, mealId ->
                        startActivity(MealActivity.newIntent(this, mealId))
                    }
                    searchResults.layoutManager = LinearLayoutManager(this)
                }, { throwable ->
                    Log.e(SearchActivity.TAG, "Failed to load search results", throwable)
                    Toast.makeText(this, R.string.failed_to_load_search_results, Toast.LENGTH_SHORT).show()
                })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_search, menu)
        val searchItem = menu.findItem(R.id.menu_recipe_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                querySubject.onNext(newText)
                return true
            }
        })
        return true
    }

    private fun getQueryObservable(): Observable<String> {
        return querySubject.distinctUntilChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}