package com.practical.products.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practical.products.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * This class is used for fragment container
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
