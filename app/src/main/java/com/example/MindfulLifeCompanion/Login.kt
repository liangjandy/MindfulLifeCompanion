package com.example.MindfulLifeCompanion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    lateinit var editTextPassword : EditText
    lateinit var editTextEmail : EditText
    lateinit var buttonReg : TextView
    lateinit var auth : FirebaseAuth
    lateinit var progressBar : ProgressBar
    lateinit var textView : Button

    fun logIn(email: String, password: String) {

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            } else {
                progressBar.visibility = View.VISIBLE
                try {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressBar.visibility = View.GONE
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(baseContext, "Login Successful.", Toast.LENGTH_SHORT).show()
                                var intent = Intent(applicationContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Handle login failure
                                val exception = task.exception as? FirebaseException
                                if (exception != null) {
                                    Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Login Failed Due to Unknown Reasons", Toast.LENGTH_SHORT).show()
                                }
                                progressBar.visibility = View.GONE
                            }
                        }
                } catch (e: FirebaseException) {

                }

            }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            var intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.username_input)
        editTextPassword = findViewById(R.id.password_input)
        buttonReg = findViewById(R.id.registertext)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.login_btn)

        buttonReg.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        textView.setOnClickListener() {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            logIn(email, password)
        }
    }
}