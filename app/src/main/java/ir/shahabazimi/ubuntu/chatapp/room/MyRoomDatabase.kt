package ir.shahabazimi.ubuntu.chatapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessageItem


@Database(entities = [MessageItem::class],version = 1,exportSchema = false)
abstract class MyRoomDatabase : RoomDatabase() {

    companion object{
       @Volatile private var instance:MyRoomDatabase?=null

        fun getInstance(ctx:Context)= instance ?: synchronized(this){
            instance ?: Room.databaseBuilder(ctx,MyRoomDatabase::class.java,"MyDb").allowMainThreadQueries().build().also { instance=it }
        }
    }

abstract fun myDao():MyDao

}