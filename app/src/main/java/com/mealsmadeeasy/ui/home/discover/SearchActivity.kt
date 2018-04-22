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
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.mealsmadeeasy.model.Filter
import com.mealsmadeeasy.ui.meal.MealActivity
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class SearchActivity : BaseActivity() {

    @Inject lateinit var mealStore: MealStore
    private var querySubject : BehaviorSubject<String> = BehaviorSubject.create()
    private val KEY_SAVED_QUERY = "SearchActivity.SavedQuery"

    private val filters = BehaviorSubject.createDefault(emptyList<Filter>())!!

    companion object {
        private const val TAG = "SearchActivity"
        fun newIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        setContentView(R.layout.activity_search)

        val toolbar = findViewById<Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        searchAndFilter()
        if (savedInstanceState != null) {
            val savedQuery = savedInstanceState.getString(KEY_SAVED_QUERY, "")
            querySubject.onNext(savedQuery)
        }
    }

    private fun searchAndFilter() {
        val searchResults = findViewById<RecyclerView>(R.id.search_results_list)

        getQueryObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .flatMapSingle { query ->
                    mealStore.search(query, filters.value)
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
        searchView.isIconified = false
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                querySubject.onNext(newText)
                return true
            }
        })

        querySubject.value?.takeIf { it.isNotEmpty() }?.let{ searchView.setQuery(it, false) }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_recipe_filter -> {
                val frag = FilterBottomDialogFragment.newInstance()
                frag.show(this.supportFragmentManager, FilterBottomDialogFragment.TAG)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getQueryObservable(): Observable<String> {
        return querySubject.distinctUntilChanged()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_SAVED_QUERY, querySubject.value)
    }

    fun setFilters(filters: List<Filter>) {
        this.filters.onNext(filters)
        searchAndFilter()
    }
}