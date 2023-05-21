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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentTransaction
import com.example.fishery.R
import com.example.fishery.component.LoadingDialog
import com.example.fishery.databinding.FragmentSignupBinding
import com.example.fishery.model.User
import com.example.fishery.screen.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupFragment : Fragment() {

    private lateinit var mBinding: FragmentSignupBinding
    private lateinit var database : FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var loadingDialog: LoadingDialog
    val FIELD_REQUIRED = "Field Required"
    var isFieldsFilled : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSignupBinding.inflate(layoutInflater)
        setUpListeners()

        database = Firebase.database
        reference = database.reference
        auth = FirebaseAuth.getInstance()
        return mBinding.root
    }
    private fun setUpListeners() {
        mBinding.toSignin.setOnClickListener {
            val loginFragment = LoginFragment()
            val transaction : FragmentTransaction = activity?.supportFragmentManager?.beginTransaction()!!
            transaction.replace(R.id.frame_layout, loginFragment).commit()
        }
        mBinding.btnSignup.setOnClickListener {
            isFieldsFilled = isFieldFilled()
            if(isFieldsFilled){
                Log.d("SIGNUP", "making signup")
                makeSignup(mBinding.email.text.toString().trim(), mBinding.password.text.toString().trim(), mBinding.name.text.toString())
            }else {
                Log.d("SIGNUP", "signInWithEmail: validation failed")
            }
        }

    }

    private fun makeSignup(email : String, password: String, name: String) {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.startLoading()

        Log.d("SIGNUP", "signInWithEmail: making signup")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGNUP", "signInWithEmail:success")
                    val user = User(name, email)
                    setUser(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGNUP", "signInWithEmail:failure", task.exception)
                    loadingDialog.stopLoading()
                    Toast.makeText(context, "Signup Failed", Toast.LENGTH_LONG).show()
                }
            }
    }
    private fun setUser(user: User){
        val userKey = auth.currentUser?.uid
        reference.child("Users").child(userKey.toString()).setValue(user).addOnCompleteListener {
            loadingDialog.stopLoading()
            startActivity(Intent(context, MainActivity::class.java))
        }
    }


    private fun isFieldFilled() : Boolean {
        val password1 = mBinding.password.text.toString().trim()
        val password2 = mBinding.rePassword.text.toString().trim()

        if(TextUtils.isEmpty(mBinding.name.text)) {
            mBinding.nameLayout.error = FIELD_REQUIRED
            return false
        }else{
            mBinding.nameLayout.error = null
        }

        if(TextUtils.isEmpty(mBinding.email.text)){
            mBinding.emailLayout.error = FIELD_REQUIRED
            return false
        }else{
            mBinding.emailLayout.error = null
        }

        if(password1.length < 6 || password2.length < 6){
            mBinding.passwordLayout.error = "Minimum 6 Characters"
            mBinding.rePasswordLayout.error = "Minimum 6 Characters"
            return false
        }else{
            mBinding.passwordLayout.error = null
            mBinding.rePasswordLayout.error = null
        }

        if(TextUtils.isEmpty(password1)){
            mBinding.passwordLayout.error = FIELD_REQUIRED
            return false
        }else {
            mBinding.passwordLayout.error = null
            if (password1 == password2){
                return true
            }
        }
        return false

    }
}