package com.practical.products.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.practical.products.ui.product.MainActivity
import com.practical.products.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Startup Activity is the first screen visible
 * to the user when the application's launched.
 */
class StartupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            delay(Constants.SPLASH_DELAY)
            val intent = Intent(this@StartupActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
