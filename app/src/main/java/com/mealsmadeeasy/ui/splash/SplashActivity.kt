package com.mealsmadeeasy.ui.splash

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.mealsmadeeasy.MealsApplication
import com.mealsmadeeasy.data.UserManager
import com.mealsmadeeasy.ui.BaseActivity
import com.mealsmadeeasy.ui.home.HomeActivity
import com.mealsmadeeasy.ui.login.LoginActivity
import com.mealsmadeeasy.ui.login.OnboardingActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import javax.inject.Inject

private const val TAG = "SplashActivity"

class SplashActivity : BaseActivity() {

    @Inject lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MealsApplication.component(this).inject(this)

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivity(LoginActivity.newIntent(this))
            finish()
        } else {
            checkOnboarded()
        }
    }

    private fun checkOnboarded() {
        userManager.isUserOnboarded()
                .bindToLifecycle(this)
                .subscribe({ onboarded ->
                    if (onboarded) {
                        startActivity(HomeActivity.newIntent(this))
                    } else {
                        startActivity(OnboardingActivity.newIntent(this))
                    }
                    finish()
                }, { throwable ->
                    Log.e(TAG, "Failed to check onboarding status", throwable)
                    showLoginError()
                })
    }

    private fun showLoginError() {
        AlertDialog.Builder(this)
                .setMessage("MealsMadeEasy couldn't log you in. Please double check your internet connection.")
                .setPositiveButton("Try again", { _, _ ->
                    checkOnboarded()
                })
                .setCancelable(false)
                .show()
    }

}