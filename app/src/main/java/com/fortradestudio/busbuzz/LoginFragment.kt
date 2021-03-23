package com.fortradestudio.busbuzz

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.fortradestudio.busbuzz.firebase.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    val mAuth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(mAuth.currentUser!=null){

            if(mAuth.currentUser!!.isEmailVerified){
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }

        }


        val firebaseRepo = FirebaseRepositoryImpl()
        val emailEditText = view.findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val signIn = view.findViewById<TextView>(R.id.signInTextView)

        passwordEditText.setOnClickListener {
            passwordEditText.setTextColor(Color.WHITE)
        }

        signIn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signInFragment)
        }

        val signUpButton = view.findViewById<AppCompatButton>(R.id.signUpButton)

        signUpButton.setOnClickListener {
            firebaseRepo.signIn(emailEditText.text.toString(),passwordEditText.text.toString()){i,user->
                if(i==0){
                    // user is already in
                    findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                }
                else if(i ==1){
                    // user is in but email not verified
                    user?.sendEmailVerification()?.addOnSuccessListener {
                        findNavController().navigate(R.id.action_loginFragment_to_verificationFragment)
                    }?.addOnFailureListener { 
                        Toast.makeText(requireContext(),"Something went wrong !! Try Again in some time",Toast.LENGTH_LONG).show()
                    }
                }
                else if(i==2){
                    // no such user found
                    passwordEditText.setTextColor(Color.RED)
                    Toast.makeText(requireContext(),"Invalid Email Or Password",Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(requireContext(),"No Such User Found ",Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}