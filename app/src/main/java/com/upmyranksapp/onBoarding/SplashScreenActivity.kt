package com.upmyranksapp.onBoarding

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.upmyranksapp.MainActivity
import com.upmyranksapp.R
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //To make screen as Full screen
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }

       // startAnimation() //To start animations

        //Handler for splash screen delay and to start next activity
        Handler(Looper.getMainLooper()).postDelayed({
            validateLogin()
        }, 3000)
    }

/*
    private fun startAnimation() {
        val animationZoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in)
        splashScreenImage.startAnimation(animationZoomIn)
        val animationSlideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        animationSlideDown.startOffset = 1000
        logoImage.startAnimation(animationSlideDown)
    }
*/

    private fun validateLogin() {
        if (MyPreferences(this).getString(Define.LOGIN_DATA).isNullOrEmpty()) {
            goToLoginScreen()
        } else {
            goToHomeScreen()
        }
    }

    private fun goToLoginScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goToHomeScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}