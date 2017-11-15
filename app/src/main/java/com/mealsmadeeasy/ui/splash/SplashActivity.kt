package com.mealsmadeeasy.ui.splash

import android.os.Bundle
import com.mealsmadeeasy.ui.BaseActivity
import com.mealsmadeeasy.ui.home.HomeActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(HomeActivity.newIntent(this))
        finish()
    }

}