package com.mealsmadeeasy.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.mealsmadeeasy.R

abstract class SingleFragmentActivity : BaseActivity() {

    private val isFragmentCreated: Boolean
        get() = supportFragmentManager.findFragmentById(R.id.single_fragment_container) != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment)

        if (!isFragmentCreated) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.single_fragment_container, onCreateFragment())
                    .commit()
        }
    }

    abstract fun onCreateFragment(): Fragment

}