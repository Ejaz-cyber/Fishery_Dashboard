package com.example.fishery.screen.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.fishery.screen.MainActivity
import com.example.fishery.screen.auth.AuthActivity
import com.google.firebase.auth.FirebaseAuth


class SplashScreen : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(android.R.color.white)

        val intent : Intent = if (auth.currentUser != null){
            Intent(this, MainActivity::class.java)
        }else{
            Intent(this, AuthActivity::class.java)
        }

        startActivity(intent)
        finish()
    }
}