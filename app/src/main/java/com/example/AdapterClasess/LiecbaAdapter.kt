package com.example.AdapterClasess

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ModelClasess.Chat
import com.example.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

class LiecbaAdapter (
        mContext: Context,
        mChatList: MutableList<Chat>,

        ) : RecyclerView.Adapter<RecyclerView.ViewHolder?>()
{

    private val mContext:Context
    private  val mChatList:List<Chat>



    init {
        this.mChatList = mChatList
        this.mContext = mContext

    }

    private val POST_TYPE_TEXT = 1
    private val POST_TYPE_IMAGE = 2


    object DateUtils {
        fun fromMillisToTimeString(millis: Long) : String {
            val format = SimpleDateFormat("dd, MMM yyyy, E hh:mm a", Locale.getDefault())
            return format.format(millis)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view : View

        return if (viewType == POST_TYPE_TEXT) {
            view =  LayoutInflater.from(mContext).inflate(R.layout.message_item_liecba_messages,parent,false)
            TextViewHolder(view)
        }
        else
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.message_item_liecba, parent, false)
            ImageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder : RecyclerView.ViewHolder, position: Int) {
        val chat: Chat = mChatList[position]
        val context = holder.itemView.context


        if (holder.itemViewType == POST_TYPE_TEXT){
            val viewHolder = holder as TextViewHolder
            viewHolder.chat_message.text = chat.getMessage()
            Picasso.get().load(chat.getSender_profile()).into(holder.profileImageDiskusia)
            viewHolder.timeText.text = chat.getTimestamp()?.let { DateUtils.fromMillisToTimeString(it.toLong()) }


            viewHolder.itemView.setOnClickListener {
                Toast.makeText(context, "Prispevok odoslany", Toast.LENGTH_SHORT).show()
            }
        }
        else if (holder.itemViewType == POST_TYPE_IMAGE)
        {
            val viewHolder = holder as ImageViewHolder
            Picasso.get().load(chat.getSender_profile()).into(holder.profileImageDiskusia2)
            Picasso.get().load(chat.getUrl()).into(holder.chat_image)
            viewHolder.timeText1.text = chat.getTimestamp1()?.let { DateUtils.fromMillisToTimeString(it.toLong()) }

            viewHolder.itemView.setOnClickListener {
                Toast.makeText(context, "Obrazok bol vlozeny", Toast.LENGTH_SHORT).show()
            }
        }

    }
    override fun getItemCount(): Int {
        return mChatList.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (mChatList[position].getMessage()?.isEmpty() == true  ) POST_TYPE_IMAGE else POST_TYPE_TEXT
    }

    inner class TextViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var profileImageDiskusia : CircleImageView
        var chat_message : TextView
        var timeText: TextView

        init {
            profileImageDiskusia = itemView.findViewById(R.id.profile_user_above1)
            chat_message = itemView.findViewById(R.id.peopleTxt1)
            timeText = itemView.findViewById(R.id.date_time4)
        }
    }
    inner class ImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var profileImageDiskusia2 : CircleImageView
        var chat_image : ImageView
        var timeText1: TextView

        init {
            profileImageDiskusia2 = itemView.findViewById(R.id.profile_user_diskusia_image1)
            chat_image = itemView.findViewById(R.id.image_message_diskusia1)
            timeText1 = itemView.findViewById(R.id.date_time3)
        }
    }
}

