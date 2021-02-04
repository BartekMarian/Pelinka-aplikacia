package com.example.Fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ModelClasess.Users
import com.example.R
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
import kotlinx.android.synthetic.main.fragment_profile_image.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileImageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var usersReference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null
    private val RequestCode : Int = 438
    private var imageUri: Uri? = null
    private var storageRef: StorageReference? = null
    private var coverChecker: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile_image, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        usersReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        usersReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: Users? = p0.getValue(Users::class.java)

                    if (context != null) {
                        view.username_set.text = user!!.getUserName()
                        Picasso.get().load(user.getProfile()).into(view.profile_image_fragment)
                        Picasso.get().load(user.getCover()).into(view.image_cover_fragment)
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        view.profile_image_fragment.setOnClickListener{
              pickImage()
        }

        view.image_cover_fragment.setOnClickListener {
            coverChecker = "cover"
            pickImage()
        }
        return view
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, RequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null)
        {
            imageUri = data.data
            Toast.makeText(context, "nahrávam...", Toast.LENGTH_LONG).show()

            uploadImageToDatabase()
        }
    }

    private fun uploadImageToDatabase() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("Obrázok sa nahráva, prosím čakajte....")
        progressBar.show()

        if (imageUri != null) {
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")

            val uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if (coverChecker == "cover") {
                        val mapCoverImg = HashMap<String, Any>()
                        mapCoverImg["cover"] = url
                        usersReference!!.updateChildren(mapCoverImg)
                        coverChecker = ""
                    }
                    else
                    {
                        val mapProfileImg = HashMap<String, Any>()
                        mapProfileImg["profile"] = url
                        usersReference!!.updateChildren(mapProfileImg)
                        coverChecker = ""
                    }
                    progressBar.dismiss()
                }
            }
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileImageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ProfileImageFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}