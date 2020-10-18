package com.example.foodiary

import com.google.firebase.auth.FirebaseAuth

object FirebaseUser {
    private var auth : FirebaseAuth? = null
    fun getInstance() : FirebaseAuth?{
        if(auth == null){
            auth = FirebaseAuth.getInstance()
        }
        return auth
    }

}