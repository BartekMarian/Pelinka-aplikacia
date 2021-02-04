package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var functions: FirebaseFunctions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar : Toolbar = findViewById(R.id.toolbar_login)
        setSupportActionBar(toolbar)
        supportActionBar!!.title ="Prihlásenie"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            val intent = Intent(this,VitajteActivity::class.java)
            startActivity(intent)
            finish()
        }


        val button = findViewById<Button>(R.id.button3)
        button.setOnClickListener {
            loginUser()
        }
        auth = FirebaseAuth.getInstance()
        functions = Firebase.functions
    }
    private fun addMessage(text: String): Task<String> {
        // Create the arguments to the callable function.
        val data = hashMapOf(
                "text" to text,
                "push" to true
        )

        return functions
                .getHttpsCallable("addMessage")
                .call(data)
                .continueWith { task ->
                    // This continuation runs on either success or failure, but if the task
                    // has failed then result will throw an Exception which will be
                    // propagated down.
                    val result = task.result?.data as String
                    result
                }
    }

    private fun loginUser()
    {

        val useremail: String = findViewById<EditText>(R.id.prihlasovacie_meno).text.toString()
        val userpassword: String = findViewById<EditText>(R.id.password).text.toString()

        if (useremail == "") {
            Toast.makeText(this, "Nezadali ste email", Toast.LENGTH_LONG).show()
        }
        else if (userpassword == "")
        {
            Toast.makeText(this, "Nezadali ste heslo", Toast.LENGTH_LONG).show()
        }
        else
        {
            auth.signInWithEmailAndPassword(useremail, userpassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful)
                        { // Pre prihlasenie si najskor treba verifikovat email
                            if (auth.currentUser?.isEmailVerified == true){
                                val intent = Intent(this,MainActivity2::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this, "Pre prihlásenie najskôr everte emailovú adresu", Toast.LENGTH_LONG).show()
                            }
                            addMessage("Test Finkcie")
                        }
                        else
                        {
                            Toast.makeText(
                                    this,
                                    "Meno alebo heslo je nespravne" + task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
        }
    }
}




