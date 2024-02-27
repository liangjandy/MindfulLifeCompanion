package com.example.MindfulLifeCompanion

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    lateinit var editTextPassword : EditText
    lateinit var editTextEmail : EditText
    lateinit var editTextConfirmPassword : EditText
    lateinit var buttonReg : Button
    lateinit var auth : FirebaseAuth
    lateinit var progressBar : ProgressBar
    lateinit var textView : TextView
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.username_input)
        editTextPassword = findViewById(R.id.password_input)
        editTextConfirmPassword = findViewById(R.id.confirmPassword_input)
        buttonReg = findViewById(R.id.register_btn)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.logintext)

        textView.setOnClickListener {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish();
        }
        buttonReg.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var email : String
            var password : String
            var confirmPass : String
            email = editTextEmail.text.toString()
            password = editTextPassword.text.toString()
            confirmPass = editTextConfirmPassword.text.toString()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this,"Enter Email", Toast.LENGTH_SHORT).show()
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this,"Enter Password", Toast.LENGTH_SHORT).show()
            }

            if (password != confirmPass) {
                Toast.makeText(this,"Passwords Do Not match", Toast.LENGTH_SHORT).show()
            }
            else {
                try {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            progressBar.visibility = View.GONE
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    this,
                                    "Authentication Successful.",
                                    Toast.LENGTH_SHORT,
                                ).show()

                                val userMap = hashMapOf(
                                    "email" to email,
                                    "password" to password
                                )

                                val userId = FirebaseAuth.getInstance().currentUser!!.uid

                                db.collection("user").document(userId).set(userMap)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "New Profile Successfully Created!", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                val intent = Intent(applicationContext, Login::class.java)
                                startActivity(intent)
                                finish();
                            }
                            else {
                                val exception = task.exception as? FirebaseException
                                if (exception != null) {
                                    Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Registration Failed Due to Unknown Reasons", Toast.LENGTH_SHORT).show()
                                }
                                progressBar.visibility = View.GONE
                            }
                        }
                    } catch (e : FirebaseException) {

                    }
            }
        }

    }
}