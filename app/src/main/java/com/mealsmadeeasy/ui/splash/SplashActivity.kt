package com.mealsmadeeasy.ui.splash

import android.os.Bundle
import com.mealsmadeeasy.ui.BaseActivity
import com.mealsmadeeasy.ui.login.LoginActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(LoginActivity.newIntent(this))
        finish()
    }
}