package ir.shahabazimi.ubuntu.chatapp.arch.message

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "messages")
data class MessageItem(
    @PrimaryKey val id: Int,
    val body: String,
    val key: String?,
    val date: String,
    val username: String
)