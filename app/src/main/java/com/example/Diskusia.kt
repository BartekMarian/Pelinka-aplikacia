package com.example

//import com.example.AdapterClasess.ChatAdapter
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.ModelClasess.Chat
import com.example.ModelClasess.Users
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_diskusia.*
import kotlinx.android.synthetic.main.message_item_left.view.*
import kotlinx.android.synthetic.main.message_item_right.view.*

class Diskusia : AppCompatActivity() {

    var userIdVisit:String = ""
    var refUsers: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
  //  var chatAdapter: ChatAdapter?= null
    var mChatList : List<Chat>? = null
   lateinit var recycler_view_chats : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diskusia)

        intent = intent
        //userIdVisit = intent.getStringExtra("visit_id").toString()
        firebaseUser = FirebaseAuth.getInstance().currentUser
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)


        recycler_view_chats = findViewById(R.id.recycler_view_chat)
        recycler_view_chats.setHasFixedSize(true)


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        recycler_view_chats.layoutManager = linearLayoutManager

        val toolbar: Toolbar = findViewById(R.id.toolbar_chat)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Diskusia"
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
                  //  retrieveMessages(firebaseUser!!.uid)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
        send_mesage_button.setOnClickListener {
            val message = text_message_chat.text.toString()
            if (message == "")
            {
                Toast.makeText(this, "Najskôr napíšte správu", Toast.LENGTH_LONG).show()
            }
            else
            {
                send_message(firebaseUser!!.uid, message)
                val adapter = GroupAdapter<ViewHolder>()
                adapter.add(UserItem(message,R.drawable.ic_menu_camera))
                recycler_view_chat.adapter = adapter
            }
           text_message_chat.setText("")
        }
        attact_image_file.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(Intent.createChooser(intent, "Vyberte obrázok"), 438)
        }






    }
    class UserItem (val name:String, val image : Int): Item<ViewHolder>(){
        override fun bind(viewHolder: ViewHolder, position: Int) {
             viewHolder.itemView.show_text_message.text = name
            viewHolder.itemView.peopleImage.load(image)
        }

        override fun getLayout()= R.layout.message_item_left
    }


    private fun send_message ( uid: String, message: String) {
        val reference = FirebaseDatabase.getInstance().reference
        val messageKey = reference.push().key
        val mesageHashMap = HashMap<String, Any?>()

        mesageHashMap["senderId"] = uid
        mesageHashMap["message"] = message
        mesageHashMap["messageId"] = messageKey
        mesageHashMap["url"] = "url"
        reference.child("Chats")
                .child(messageKey!!)
                .setValue(mesageHashMap)

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

                    val messageHashMap = HashMap<String, Any?>()
                    messageHashMap["senderId"] = firebaseUser!!.uid
                    messageHashMap["message"] = "Posielam fotku"
                    messageHashMap["messageId"] = messageId
                    messageHashMap["url"] = url

                    ref.child("Chat Images").child(messageId!!).setValue(messageHashMap)

                    progressBar.dismiss()
                }
            }
        }
    }
//    private fun retrieveMessages(senderId: String)
//    {
//        mChatList = ArrayList()
//        val reference = FirebaseDatabase.getInstance().reference.child("Chats")
//
//        reference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                (mChatList as ArrayList<Chat>).clear()
//                for (snapshot in p0.children) {
//                    val chat = snapshot.getValue(Chat::class.java)
//
//                    if (chat!!.getUser().equals(senderId)) {
//                        (mChatList as ArrayList<Chat>).add(chat)
//                        chatAdapter = ChatAdapter(this@Diskusia, mChatList as ArrayList<Chat>)
//                        this@Diskusia.recycler_view_chats.adapter = chatAdapter
//
//                    }
//                }
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//
//        })
//    }


}