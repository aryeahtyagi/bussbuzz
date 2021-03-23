package com.fortradestudio.busbuzz.firebase

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseUser

interface FirebaseRepository {

    fun authenticateUser(email:String , password:String,verificationLinkSend:(Boolean)->Unit,methodCompleteCallback: (Boolean)->Unit)

    fun looperCheckIfEmailVerified(onEmailVerified:()->Unit)

    // here int `0` means -> user email is verified , `1` -> user email is not verified , `2`->user is not registered
    fun signIn(email:String, password: String,accountVerifiedTask:(Int,FirebaseUser?)->Unit)


}