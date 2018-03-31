package com.mealsmadeeasy.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import com.mealsmadeeasy.R
import com.mealsmadeeasy.ui.BaseActivity
import com.mealsmadeeasy.ui.home.glance.WeekAtAGlanceFragment
import com.mealsmadeeasy.ui.home.discover.DiscoverFragment
import com.mealsmadeeasy.ui.home.grocery.GroceryListFragment
import com.mealsmadeeasy.ui.home.profile.ProfileFragment
import com.mealsmadeeasy.utils.first
import com.mealsmadeeasy.utils.forEach

private const val KEY_SAVED_FRAGMENT = "HomeActivity.SelectedPage"

class HomeActivity : BaseActivity() {

    private val homeDrawerNavigationView: NavigationView
        get() = findViewById(R.id.home_drawer_navigation_view)

    private val homeDrawerLayout: DrawerLayout
        get() = findViewById(R.id.home_drawer_layout)

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val savedFragment = savedInstanceState?.getInt(KEY_SAVED_FRAGMENT, 0)?.takeIf { it != 0 }
        setSelectedFragment(savedFragment ?: R.id.menu_item_week_at_a_glance,
                navigateTo = savedInstanceState == null)

        homeDrawerNavigationView.setNavigationItemSelectedListener { menuItem ->
            setSelectedFragment(menuItem.itemId)
            homeDrawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.let {
            it.setHomeAsUpIndicator(R.drawable.ic_menu_24dp)
            it.setDisplayHomeAsUpEnabled(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        homeDrawerNavigationView.menu.first { it.isChecked }?.itemId?.let {
            outState.putInt(KEY_SAVED_FRAGMENT, it)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        homeDrawerLayout.openDrawer(GravityCompat.START)
        return true
    }

    private fun setSelectedFragment(menuItemId: Int, navigateTo: Boolean = true) {
        val menu = homeDrawerNavigationView.menu
        if (menu.first { it.isChecked }?.itemId == menuItemId) {
            return
        }

        menu.forEach { it.isChecked = (it.itemId == menuItemId) }

        val title = getString(when (menuItemId) {
            R.id.menu_item_grocery_list -> R.string.grocery_list
            R.id.menu_item_user_profile -> R.string.profile
            R.id.menu_item_discover -> R.string.discover
            else -> R.string.app_name
        })

        supportActionBar?.title = title

        if (navigateTo) {
            val fragment = when (menuItemId) {
                R.id.menu_item_week_at_a_glance -> WeekAtAGlanceFragment.newInstance()
                R.id.menu_item_grocery_list -> GroceryListFragment.newInstance()
                R.id.menu_item_user_profile -> ProfileFragment.newInstance()
                R.id.menu_item_discover -> DiscoverFragment.newInstance()
                else -> throw IllegalArgumentException("No fragment found for $menuItemId")
            }

            supportFragmentManager.beginTransaction()
                    .replace(R.id.home_fragment_container, fragment)
                    .commit()
        }
    }

}