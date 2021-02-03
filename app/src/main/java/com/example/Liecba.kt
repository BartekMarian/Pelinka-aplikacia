package com.example


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.AdapterClasess.LiecbaAdapter
import com.example.ModelClasess.Chat
import com.example.ModelClasess.Users
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_diskusia.*
import kotlinx.android.synthetic.main.activity_liecba.*
import kotlinx.android.synthetic.main.message_item_image.view.*
import kotlinx.android.synthetic.main.message_item_left.*
import kotlinx.android.synthetic.main.message_item_left.view.*
import kotlinx.android.synthetic.main.message_item_liecba.view.*
import kotlinx.android.synthetic.main.message_item_liecba_messages.view.*
import java.util.*
import kotlin.collections.HashMap


class Liecba :  AppCompatActivity()    {

    companion object {
        val TAG = "Chatlog"
    }

    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    private var storageRef: StorageReference? = null


    var chatAdapter: LiecbaAdapter?= null
    var mChatList: MutableList<Chat>? = null

    lateinit var recycler_view_chats: RecyclerView
    val adapter =  GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liecba)


        intent = intent
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User_Images")

        recycler_view_chats = findViewById(R.id.recycler_view_liecba)
        recycler_view_chats.setHasFixedSize(true)

        val linearLayoutManager = LinearLayoutManager(applicationContext)
        recycler_view_chats.layoutManager = linearLayoutManager
        linearLayoutManager.stackFromEnd = true


        val toolbar: Toolbar = findViewById(R.id.toolbar_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Liečba"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, Diskusia_menu::class.java)
            startActivity(intent)
            finish()
        }

        refUsers!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: Users? = p0.getValue(Users::class.java)
                    user_name_chat1.text = user!!.getUserName()
                    Picasso.get().load(user.getProfile()).into(profile_user_chat1)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        runOnUiThread {
            retrieveMessages(senderId = String())
        }



        runOnUiThread {
            send_mesage_button1.setOnClickListener {
                val message: String = text_message_chat1.text.toString()
                if (message == "") {
                    Toast.makeText(this, "Najskôr napíšte správu", Toast.LENGTH_LONG).show()
                } else {
                    send_message(firebaseUser!!.uid, message, profile_user = String())
                    refUsers!!.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                val user: Users? = p0.getValue(Users::class.java)
                                val chat: Chat? = p0.getValue(Chat::class.java)
                                if (user != null && chat != null) {
                                    adapter.add(UserItem_liecba(user, message))

                                }
                            }
                        }

                        override fun onCancelled(p0: DatabaseError) {
                        }
                    })
                    recycler_view_chats.adapter = adapter
                }
                text_message_chat1.setText("")
            }
        }

        runOnUiThread {
            attact_image_file1.setOnClickListener {

                pick_image()

                refUsers!!.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            val user: Users? = p0.getValue(Users::class.java)
                            val url: Chat? = p0.getValue(Chat::class.java)
                            if (user != null && url != null) {
                                adapter.add(User_chat_liecba(user, url))

                            }
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
                recycler_view_chats.adapter = adapter
            }
        }
    }

    private fun pick_image() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Vyberte obrázok"), 438)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data!!.data != null) {
            val progressBar = ProgressDialog(this)
            progressBar.setMessage("Obrázok sa nahráva, prosím čakajte....")
            progressBar.show()

            val fileUri = data.data
            val storageReference = FirebaseStorage.getInstance()
                    .reference.child("Chat Images")
            val ref = FirebaseDatabase.getInstance().reference
            val messageId = ref.push().key
            val filePath = storageReference.child("$messageId.jpg")

            val uploadTask: StorageTask<*>
            uploadTask = filePath.putFile(fileUri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation filePath.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()
                    val profile_image_ref = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
                    var profile_user = ""
                    val timestamp1 = Calendar.getInstance().timeInMillis

                    profile_image_ref.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                val messageHashMap = HashMap<String, Any?>()
                                val user: Users? = p0.getValue(Users::class.java)
                                if (user != null) {
                                    profile_user = user.getProfile().toString()
                                }
                                messageHashMap["sender_profile"] = profile_user
                                messageHashMap["senderId"] = firebaseUser!!.uid
                                messageHashMap["connection"] = "PFXY"
                                messageHashMap["messageId"] = messageId
                                messageHashMap["url"] = url
                                messageHashMap["timestamp1"] = timestamp1
                                ref.child("LiecbaList")
                                        .child(messageId!!)
                                        .setValue(messageHashMap)


                            }
                            progressBar.dismiss()
                        }

                        override fun onCancelled(p0: DatabaseError) {

                        }

                    })
                }
            }
        }
    }

    private fun send_message(uid: String, message: String, profile_user: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key
        val mesageHashMap = HashMap<String, Any?>()
        val timestamp = Calendar.getInstance().timeInMillis
        val profile_image_ref = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        var profile_user = ""
        profile_image_ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: Users? = p0.getValue(Users::class.java)
                    if (user != null) {
                        profile_user = user.getProfile().toString()
                        mesageHashMap["sender_profile"] = profile_user
                        mesageHashMap["senderId"] = uid
                        mesageHashMap["message"] = message
                        mesageHashMap["messageId"] = messageKey
                        mesageHashMap["url"] = "https://firebasestorage.googleapis.com/v0/b/pelova-apka.appspot.com/o/Chat_image.png?alt=media&token=91779606-fef6-4787-a1a0-9fdde4272c05"
                        mesageHashMap["timestamp"] = timestamp
                        reference.child("LiecbaList")
                                .child(messageKey!!)
                                .setValue(mesageHashMap)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun retrieveMessages(senderId: String)
    {
        mChatList = ArrayList()

        val reference = FirebaseDatabase.getInstance().reference.child("LiecbaList")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                (mChatList as ArrayList<Chat>).clear()

                for (snapshot in p0.children) {
                    val chat = snapshot.getValue(Chat::class.java)
                    if (chat!!.getUser().equals(senderId)) {
                        (mChatList as ArrayList<Chat>).add(chat)
                        chatAdapter = LiecbaAdapter(this@Liecba, mChatList as ArrayList<Chat>)
                        this@Liecba.recycler_view_chats.adapter = chatAdapter
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}


class UserItem_liecba(val user: Users, val name: String) : Item<ViewHolder>() {

    constructor() : this(Users(), "")


    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.peopleTxt1.text = name
        Picasso.get().load(user.getProfile()).into(viewHolder.itemView.profile_user_above1)
    }

    override fun getLayout(): Int = R.layout.message_item_liecba_messages
}

class User_chat_liecba(val user: Users, val url: Chat): Item<ViewHolder>(){

    constructor() : this(Users(), Chat())

    override fun bind(viewHolder: ViewHolder, position: Int) {
        Picasso.get().load(user.getProfile()).into(viewHolder.itemView.profile_user_diskusia_image1)
        //      Picasso.get().load(url.getUrl()).into(viewHolder.itemView.image_message_diskusia)
    }
    override fun getLayout(): Int = R.layout.message_item_liecba
}

