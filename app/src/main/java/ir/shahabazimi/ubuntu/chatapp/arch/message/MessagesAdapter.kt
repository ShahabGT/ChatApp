package ir.shahabazimi.ubuntu.chatapp.arch.message

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.emoji.widget.EmojiTextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.view.SimpleDraweeView
import ir.shahabazimi.ubuntu.chatapp.R
import ir.shahabazimi.ubuntu.chatapp.classes.MySharedPreference
import java.text.SimpleDateFormat
import java.util.*


class MessagesAdapter(private val ctx: Context) :
    PagedListAdapter<MessageItem, MessagesAdapter.ViewHolder>(diffCallback) {

    private var selectedPositionId: Int = -1

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<MessageItem>() {
            override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem) =
                oldItem.id == newItem.id


            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem) =
                oldItem == newItem

        }
    }


    private val username = MySharedPreference.getInstance(ctx).getUser()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_message, parent, false)
        )

    @Suppress("DEPRECATION")
    @SuppressLint("RtlHardcoded")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        if (item != null) {
            holder.message.text =
                Base64.decode(item.body, Base64.DEFAULT).toString(charset("UTF-8"))
            holder.time.text = addLocalTime(item.date).substring(11, 16)
            if (item.username?.toLowerCase(Locale.ENGLISH) == username?.toLowerCase(Locale.ENGLISH)) {
                holder.card.gravity = Gravity.RIGHT
                holder.avatar.visibility = View.GONE
                holder.constraint.background = ctx.resources.getDrawable(R.drawable.shape_me)
            } else {
                holder.card.gravity = Gravity.LEFT
                holder.avatar.visibility = View.VISIBLE
                holder.constraint.background = ctx.resources.getDrawable(R.drawable.shape_other)
                holder.avatar.setImageURI(
                    Uri.parse(
                        "https://radical-app.ir/chatapp/avatars/${item.username?.toLowerCase(
                            Locale.ENGLISH
                        )}.jpg"
                    )
                )
            }

            holder.itemView.setOnLongClickListener {
                selectedPositionId = item.id!!
                false
            }
        }
    }


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var message: EmojiTextView = v.findViewById(R.id.row_message)

        var time: TextView = v.findViewById(R.id.row_time)
        var card: LinearLayout = v.findViewById(R.id.row_card)
        var constraint: ConstraintLayout = v.findViewById(R.id.row_constraint)
        var avatar: SimpleDraweeView = v.findViewById(R.id.row_avatar)

        init {
            v.setOnCreateContextMenuListener { a, _, _ ->
                a.add(selectedPositionId, v.id, 0, "Copy")
                a.add(selectedPositionId, v.id, 0, "Delete")
                a.add(selectedPositionId, v.id, 0, "Info")
            }
        }


    }

    private fun addLocalTime(stringDate: String?): String {
        if (!stringDate.isNullOrEmpty()) {
            val year = stringDate.substring(0, 4).toInt()
            val month = stringDate.substring(5, 7).toInt()
            val day = stringDate.substring(8, 10).toInt()
            val hour = stringDate.substring(11, 13).toInt()
            val minute = stringDate.substring(14, 16).toInt()
            val sec = stringDate.substring(17, 19).toInt()

            val formater = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
            val cal = Calendar.getInstance()
            cal.set(year, month - 1, day, hour, minute, sec)
            cal.add(Calendar.HOUR_OF_DAY, 3)
            cal.add(Calendar.MINUTE, 30)
            return formater.format(cal.time)
        }
        return ""
    }
}