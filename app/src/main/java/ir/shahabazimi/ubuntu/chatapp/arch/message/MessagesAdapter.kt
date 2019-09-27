package ir.shahabazimi.ubuntu.chatapp.arch.message

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.emoji.widget.EmojiTextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.shahabazimi.ubuntu.chatapp.R
import kotlinx.android.synthetic.main.row_message.view.*
import java.util.*
import android.annotation.SuppressLint
import android.net.Uri
import android.util.Base64
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.Visibility
import com.facebook.drawee.view.SimpleDraweeView


class MessagesAdapter(private val ctx:Context) :PagedListAdapter<MessageItem,MessagesAdapter.ViewHolder>(diffCallback){


    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<MessageItem>() {
            override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem)= oldItem.id==newItem.id


            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem)=oldItem==newItem

        }
    }






    private val username = MySharedPreference.getInstance(ctx).getUser()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder=
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_message,parent,false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item=getItem(position)
        if(item!=null){
            holder.message.text=Base64.decode(item.body,Base64.DEFAULT).toString(charset("UTF-8"))
            holder.time.text=item.date.substring(11,16)
            if(item.username.toLowerCase(Locale.ENGLISH)==username?.toLowerCase(Locale.ENGLISH)){
                holder.card.gravity=Gravity.RIGHT
                holder.avatar.visibility=View.GONE
                holder.constraint.background=ctx.resources.getDrawable(R.drawable.shape_me)
            }else{
                holder.card.gravity=Gravity.LEFT
                holder.avatar.visibility=View.VISIBLE
                holder.constraint.background=ctx.resources.getDrawable(R.drawable.shape_other)
                holder.avatar.setImageURI(Uri.parse("https://shahabazimi.ir/chatapp/avatars/${item.username}.jpg"))
            }
        }
    }






    class ViewHolder(v:View):RecyclerView.ViewHolder(v){

         var message:EmojiTextView = v.findViewById(R.id.row_message)
         var time: TextView = v.findViewById(R.id.row_time)
         var card:LinearLayout = v.findViewById(R.id.row_card)
         var constraint:ConstraintLayout = v.findViewById(R.id.row_constraint)
         var avatar:SimpleDraweeView = v.findViewById(R.id.row_avatar)


    }
}