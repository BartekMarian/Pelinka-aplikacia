package com.example

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val toolbar: Toolbar = findViewById(R.id.toolbar_reset)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Reset hesla"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_reset.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        mAuth = FirebaseAuth.getInstance()

        reset_password_button.setOnClickListener {
            val email = editTextTextEmailAddress.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Najskôr vložte Vašu emailovú adresu", Toast.LENGTH_SHORT).show()
            } else {
                mAuth!!.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@ResetPassword, "Obnovenie hesla bolo úspešné, potvrdte došlý link", Toast.LENGTH_SHORT).show()
                            Thread.sleep(2_000)
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@ResetPassword, "Obnovenie hesla sa nepodarilo, skúste znovu!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


}