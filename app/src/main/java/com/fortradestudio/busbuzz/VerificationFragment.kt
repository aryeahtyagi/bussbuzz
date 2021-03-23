package com.fortradestudio.busbuzz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fortradestudio.busbuzz.firebase.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth


class VerificationFragment : Fragment() {

    private val firebaseRepository = FirebaseRepositoryImpl()
    private val mAuth = FirebaseAuth.getInstance()
    private lateinit var verifiedBtn : AppCompatButton
    private var isEmailVerified = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifiedBtn = view.findViewById<AppCompatButton>(R.id.verifiedBtn)

        firebaseRepository.looperCheckIfEmailVerified {
            // when email is verified
            isEmailVerified = true
            verifiedBtn.visibility = View.VISIBLE
        }

        verifiedBtn.setOnClickListener {
            if (isEmailVerified) {
                findNavController().navigate(R.id.action_verificationFragment_to_homeFragment)
            }else{
                Toast.makeText(requireContext(),"Verify Email Please!!",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if(mAuth.currentUser?.isEmailVerified==true){
            isEmailVerified = true
            verifiedBtn.visibility = View.VISIBLE
        }

    }


}