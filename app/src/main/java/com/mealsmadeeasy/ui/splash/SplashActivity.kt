package com.mealsmadeeasy.ui.splash

import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.mealsmadeeasy.ui.BaseActivity
import com.mealsmadeeasy.ui.login.LoginActivity
import com.mealsmadeeasy.ui.home.HomeActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(LoginActivity.newIntent(this))
        } else {
            startActivity(HomeActivity.newIntent(this))
        }
        finish()
    }
}