package ir.shahabazimi.ubuntu.chatapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessageItem


@Database(entities = [MessageItem::class],version = 1,exportSchema = false)
abstract class MyRoomDatabase : RoomDatabase() {

    companion object{
        private var instance:MyRoomDatabase?=null
                fun getInstance(ctx:Context):MyRoomDatabase?{
                    if(instance==null)
                        instance= Room.databaseBuilder(ctx,MyRoomDatabase::class.java,"MyDb")
                            .allowMainThreadQueries()
                            .build()

                    return instance

                }

    }

abstract fun myDao():MyDao

}