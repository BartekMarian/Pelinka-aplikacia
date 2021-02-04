package com.example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class Registracia : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth             //autentifikácia
    var refUser: DatabaseReference?=null
    private var firebaseUserID: String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registracia)   // vytvorenie designu stránky zo súboru registracia.xml

        val toolbar: Toolbar = findViewById(R.id.toolbar_registracia)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registrácia"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, VitajteActivity::class.java)
            startActivity(intent)
            finish()
        }

        val buttonReg = findViewById<Button>(R.id.button_registracia)
        buttonReg.setOnClickListener {
            registerUser()    // funkcia pre uloženie údajov
        }
        Firebase.initialize(application)
        auth = FirebaseAuth.getInstance()

    }

    private fun registerUser() {

        val username: String = findViewById<EditText>(R.id.username_register).text.toString()
        val userpassword: String = findViewById<EditText>(R.id.password_register).text.toString()
        val useremail = findViewById<EditText>(R.id.emailAddress_register).text.toString()
        val userphone: String = findViewById<EditText>(R.id.phone_register).text.toString()

        println("test registracie" + useremail+username+userpassword+userphone)
 //Vstupy od užívateľa sa uložia do premenných username,userpassword, useremail a userphone

        if (username == "")   // pole nesmie byť prázdne
        {
            Toast.makeText(this, "Nezadali ste meno", Toast.LENGTH_LONG).show()
        } else if (userpassword == "") {
            Toast.makeText(this, "Nezadali ste heslo", Toast.LENGTH_LONG).show()
        } else if (useremail == "") {
            Toast.makeText(this, "Nezadali ste email", Toast.LENGTH_LONG).show()
        } else if (userphone == "") {
            Toast.makeText(this, "Nezadali ste mobil", Toast.LENGTH_LONG).show()
        } else {
            auth.createUserWithEmailAndPassword(useremail, userpassword)  // uloženie údajov do DB
                    .addOnCompleteListener { task ->
                        println("print nieco................................OK")
                        verification_of_email()
                        if (task.isSuccessful) {
                            firebaseUserID = auth.currentUser!!.uid
                            refUser = FirebaseDatabase.getInstance().reference.child("Users")
                                    .child(firebaseUserID)
                            val userHashMap = HashMap<String, Any>()
                            userHashMap["uid"] = firebaseUserID
                            userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/pelova-apka.appspot.com/o/cover.jpg?alt=media&token=e4477764-f1c8-452e-852a-2927f3289771"
                            userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/pelova-apka.appspot.com/o/profile.png?alt=media&token=f626c9a7-fee7-4f96-8986-903dc8a8d39b"
                            userHashMap["username"] = username
                            userHashMap["phone"] = userphone
                            userHashMap["useremail"] = useremail
                            userHashMap["password"] = userpassword
                            refUser!!.updateChildren(userHashMap)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Ak sa registrácia podarila sme presmerovaný na prihlásenie
                                            val intent = Intent(this, MainActivity::class.java)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                            startActivity(intent)
                                        }
                                    }
                            println("print nieco................................OK"+firebaseUserID)
                        } else {
                            Toast.makeText(
                                    this,
                                    "Registrácia sa nepodarila" + task.exception!!.message.toString(),
                                    Toast.LENGTH_LONG
                            ).show()
                        }
                    }
        }

    }
    private fun verification_of_email (){
        val user = FirebaseAuth.getInstance().currentUser
        user?.sendEmailVerification()?.addOnCompleteListener{
            Toast.makeText(this, "Registrácia prebehla úspešne, prosím overte Vašu emailovú adresu", Toast.LENGTH_LONG).show()
            println("print nieco................................OK")
        }

    }
}