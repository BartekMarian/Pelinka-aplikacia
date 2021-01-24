//package com.example.AdapterClasess
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.ModelClasess.Chat
//import com.example.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.squareup.picasso.Picasso
//import de.hdodenhof.circleimageview.CircleImageView
//
//class ChatAdapter(
//        mContext: Context,
//        mChatList: List<Chat>
//) : RecyclerView.Adapter<ChatAdapter.ViewHolder?>()
//{
//
//    private val mContext:Context = mContext
//    private  val mChatList:List<Chat> = mChatList
//
//    // private val imageUrl: String
//    var firebaseUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
//
//    init {
//        // this.imageUrl = imageUrl
//    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//
//
//            val view: View = LayoutInflater.from(mContext).inflate(R.layout.message_item_right,parent,false)
//           return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
//
//        val chat: Chat = mChatList[i]
//
//        Picasso.get().load(chat.getUser()).into(holder.profile_user_image)
//
//
//        //images messages
//        if (chat.getMessage().equals("Posielam fotku") && chat.getUrl().equals("url")) {
//            // image message - right side
//            if (chat.getUser().equals(firebaseUser!!.uid)) {
//                holder.show_text_message!!.visibility = View.GONE
//                holder.right_image_view!!.visibility = View.VISIBLE
//                Picasso.get().load(chat.getUrl()).into(holder.right_image_view)
//            }
//            //text messages
//            else
//            {
//                holder.show_text_message!!.text = chat.getMessage()
//            }
//        }
//    }
//
//        override fun getItemCount(): Int {
//            return R.layout.message_item_right
//
//        }
//
//
//        class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
//            var profile_user_image:CircleImageView?=null
//            var show_text_message :TextView? =null
//            var right_image_view :ImageView?=null
//
//            init {
//                profile_user_image = itemView.findViewById(R.id.profile_user_image)
//                show_text_message =itemView.findViewById(R.id.show_text_message)
//                right_image_view = itemView.findViewById(R.id.right_image_view)
//            }
//        }
//}
//
