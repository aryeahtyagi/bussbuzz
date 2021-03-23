package com.fortradestudio.busbuzz.firebase

import android.os.Looper
import android.util.Log
import com.google.firebase.auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler

class FirebaseRepositoryImpl : FirebaseRepository {

    private val mAuth = FirebaseAuth.getInstance()

    override fun authenticateUser(
        email: String,
        password: String,
        verificationLinkSend: (Boolean) -> Unit,
        methodCompleteCallback: (Boolean) -> Unit
    ) {

        if(email.isEmpty() || password.isEmpty()){
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                synchronized(this) {
                    if (it.user?.isEmailVerified == false) {
                        // here email is not verified

                        it.user?.sendEmailVerification()?.addOnSuccessListener {
                            verificationLinkSend(true)
                            methodCompleteCallback(true)
                        }?.addOnFailureListener {
                            verificationLinkSend(false)
                        }
                    } else {
                        verificationLinkSend(true)
                        methodCompleteCallback(true)
                    }
                }
            }
            .addOnFailureListener {
                methodCompleteCallback(false)
            }
    }

    override fun looperCheckIfEmailVerified(onEmailVerified: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            mAuth.currentUser?.reload()?.addOnSuccessListener {
                //here reload is success so we check if email is verified
                when (mAuth.currentUser?.isEmailVerified) {
                    true -> {
                        // when email is verified
                        onEmailVerified()
                    }
                    false -> {
                        // when email is not verified
                        Thread.sleep(1000L)
                        looperCheckIfEmailVerified(onEmailVerified)
                    }
                }
            }
        }
    }

    override fun signIn(
        email: String,
        password: String,
        accountVerifiedTask: (Int, FirebaseUser?) -> Unit
    ) {

        if(email.isEmpty() || password.isEmpty()){
            return
        }

        mAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                // here sign in is success
                if(it.user?.isEmailVerified==true){
                    // if this user's email is verified
                    accountVerifiedTask(0,it.user)
                }
                else{
                    // if user's email is not verified
                    accountVerifiedTask(1,it.user)
                }
            }
            .addOnFailureListener {
                if(it is FirebaseAuthInvalidCredentialsException){
                    accountVerifiedTask(2,null)
                }else{
                    accountVerifiedTask(3,null)
                }
            }
    }


}