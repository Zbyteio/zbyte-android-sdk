package com.zbyte.sampleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.zbyte.nftsdk.TOKEN
import com.zbyte.sampleapp.databinding.ActivityZbyteBinding

class ZByteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityZbyteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZbyteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getFCMToken()
    }

    /**
     * Function to get the FCM Token for Push Notification
     */
    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.e("Token::", token)
                TOKEN = token //Storing the token into the NFT SDK by accessing the 'TOKEN' variable
            } else {
                return@addOnCompleteListener
            }
        }
    }
}