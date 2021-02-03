package com.example

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.ModelClasess.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_diskusia.profile_user_chat
import kotlinx.android.synthetic.main.activity_diskusia.user_name_chat
import kotlinx.android.synthetic.main.avtivity_diskusia_menu.*

class Diskusia_menu : AppCompatActivity()  {
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.avtivity_diskusia_menu)
        intent = intent
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)


        val toolbar: Toolbar = findViewById(R.id.toolbar_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Príspevky"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        }

        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: Users? = p0.getValue(Users::class.java)
                    user_name_chat.text = user!!.getUserName()
                    Picasso.get().load(user.getProfile()).into(profile_user_chat)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        card_diskusia.setOnClickListener{
            val intent = Intent(this, Diskusia::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
            finish()
        }

        card_liecba.setOnClickListener{
            val intent = Intent(this, Liecba::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
            finish()
        }

        card_aktuality.setOnClickListener{
            val intent = Intent(this, Aktuality::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            this.startActivity(intent)
            finish()
        }
    }
}