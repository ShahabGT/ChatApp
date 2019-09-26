package ir.shahabazimi.ubuntu.chatapp.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessageItem

@Dao
interface MyDao {

    @Query("SELECT * FROM messages ORDER BY date DESC")
    fun selectPaged(): DataSource.Factory<Int, MessageItem>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item:MessageItem)

}