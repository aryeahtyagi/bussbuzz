package com.fortradestudio.busbuzz

import android.graphics.Color
import android.graphics.Color.red
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.fortradestudio.busbuzz.firebase.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignInFragment : Fragment() {

    private val mAuth : FirebaseUser? by lazy {
        FirebaseAuth.getInstance().currentUser
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (mAuth != null) {
           // there is a user so
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }

        val firebaseRepository = FirebaseRepositoryImpl()

        val signIn = view.findViewById<AppCompatButton>(R.id.signInButton)
        val username = view.findViewById<EditText>(R.id.emailEditText)
        val password = view.findViewById<EditText>(R.id.passwordEditText)
        val confirm = view.findViewById<EditText>(R.id.confirmPasswordText)
        val navigateLogIn = view.findViewById<TextView>(R.id.backToLogin)



        confirm.addTextChangedListener {
            if(password.text.toString()==it.toString()){
                // password both match
                password.setTextColor(Color.GREEN)
                confirm.setTextColor(Color.GREEN)
            }else{
                confirm.setTextColor(Color.RED)
            }
        }

        navigateLogIn.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_loginFragment)
        }

        signIn.setOnClickListener {

            if(confirm.text.toString() == password.text.toString()){
                firebaseRepository.authenticateUser(
                    username.text.toString(),
                    password.text.toString(),{
                        if (it){
                            Toast.makeText(requireContext(),"Verification Link Sent!!",Toast.LENGTH_SHORT).show()

                         }
                        else{
                            Toast.makeText(requireContext(),"Something went Wrong",Toast.LENGTH_LONG).show()
                        }
                    }){
                    if(it){
                        findNavController().navigate(R.id.action_signInFragment_to_verificationFragment)
                    }
                    else{
                        Toast.makeText(requireContext(),"Something went wrong !!",Toast.LENGTH_LONG).show()
                    }
                }
            }
            else{
                confirm.requestFocus()
                confirm.setTextColor(Color.RED)
            }

        }
    }

    private fun sendEmailLink(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnSuccessListener {
                findNavController().navigate(R.id.action_signInFragment_to_verificationFragment)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Something went wrong!!",Toast.LENGTH_LONG).show()
            }
    }

}