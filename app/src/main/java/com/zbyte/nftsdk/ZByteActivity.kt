package com.zbyte.nftsdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zbyte.nftsdk.databinding.ActivityZbyteBinding

class ZByteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityZbyteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZbyteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}