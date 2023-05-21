package com.example.fishery.screen.auth

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.fishery.R
import com.example.fishery.component.LoadingDialog
import com.example.fishery.databinding.FragmentLoginBinding
import com.example.fishery.screen.MainActivity
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var mBinding : FragmentLoginBinding
    val FIELD_REQUIRED = "Field Required"
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentLoginBinding.inflate(layoutInflater)
        setUpListeners()

        auth = FirebaseAuth.getInstance()

        return mBinding.root
    }

    private fun setUpListeners() {
        mBinding.toSignup.setOnClickListener {
            val fragmentSignup = SignupFragment()
            val transaction : FragmentTransaction = activity?.supportFragmentManager?.beginTransaction()!!
            transaction.replace(R.id.frame_layout, fragmentSignup).commit()
        }

        mBinding.btnLogin.setOnClickListener {
            if(validateEmail() && validatePassword()){
                // make login
                makeLogin(mBinding.email.text.toString().trim(), mBinding.password.text.toString().trim())
            }
        }

        mBinding.googleSigninCard.setOnClickListener {
            Toast.makeText(context, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
        }
    }
    private fun makeLogin(email : String, password: String) {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LOGIN", "createUserWithEmail:success")
                    val user = auth.currentUser
                    loadingDialog.stopLoading()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LOGIN", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_LONG,
                    ).show()
                    loadingDialog.stopLoading()
                }
            }
    }

    private fun validateEmail() : Boolean{
        return if(TextUtils.isEmpty(mBinding.email.text)){
            mBinding.emailLayout.error = FIELD_REQUIRED
            false
        }else{
            mBinding.emailLayout.error = null
            true
        }
    }

    private fun validatePassword() : Boolean {
        return if(TextUtils.isEmpty(mBinding.password.text)){
            mBinding.passwordLayout.error = FIELD_REQUIRED
            false
        }else {
            mBinding.passwordLayout.error = null
            true
        }
    }
}