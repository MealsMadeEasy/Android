package com.mealsmadeeasy.ui.login

import android.content.Context
import android.content.Intent
import com.mealsmadeeasy.ui.SingleFragmentActivity
import com.mealsmadeeasy.ui.home.profile.ProfileFragment

class OnboardingActivity : SingleFragmentActivity() {

    companion object {
        fun newIntent(context: Context) = Intent(context, OnboardingActivity::class.java)
    }

    override fun onCreateFragment() = ProfileFragment.newInstance(isOnboarding = true)

}