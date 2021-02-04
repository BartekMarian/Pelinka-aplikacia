package com.example.AdapterClasess

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ModelClasess.Users
import com.example.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

open class UserAdapter(
        mContext :Context,
        mUsers: List<Users>
        ) :RecyclerView.Adapter<UserAdapter.ViewHolder?>()
{

    private val mContext:Context
    private val mUsers:List<Users>

    init {
        this.mUsers = mUsers
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_item_layout,parent,false)
    return UserAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val user: Users = mUsers[i]
        holder.userNameTXT.text = user!!.getUserName()
        Picasso.get().load(user.getProfile()).into(holder.profilImageView)
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }
    class ViewHolder(itemView :View):RecyclerView.ViewHolder(itemView)
    {

        var userNameTXT: TextView
        var profilImageView:CircleImageView

        init{
            userNameTXT = itemView.findViewById(R.id.user_name)
            profilImageView = itemView.findViewById(R.id.profile_image)
        }


    }


}