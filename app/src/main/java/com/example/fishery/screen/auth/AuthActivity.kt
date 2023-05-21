package com.example.fishery.screen.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fishery.databinding.ActivityAuthBinding
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal

class AuthActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityAuthBinding
    private val fragmentLogin = LoginFragment()
    private val fragmentSignup = SignupFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val transaction : FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(mBinding.frameLayout.id, fragmentLogin).commit()

        NoInternetDialogSignal.Builder(
            this,
            lifecycle
        ).apply{}.build()
    }
}