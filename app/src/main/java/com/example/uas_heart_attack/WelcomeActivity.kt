package com.example.uas_heart_attack


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val buttonWelcome: Button = findViewById(R.id.buttonwelcome)
        buttonWelcome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("destination", R.id.nav_tentang)
            startActivity(intent)
        }
    }
}
