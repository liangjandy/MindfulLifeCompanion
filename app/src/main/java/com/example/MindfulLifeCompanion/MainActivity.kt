package com.example.MindfulLifeCompanion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var logoutButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutButton = findViewById(R.id.logout_btn)

        logoutButton.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish();
        }
    }
}