package ir.shahabazimi.ubuntu.chatapp.arch.message

import androidx.paging.PagedList
import ir.shahabazimi.ubuntu.chatapp.data.RetrofitClient
import ir.shahabazimi.ubuntu.chatapp.enqueue
import ir.shahabazimi.ubuntu.chatapp.room.MyRoomDatabase
import java.util.concurrent.Executors

class MessageBoundry(private val db:MyRoomDatabase) :PagedList.BoundaryCallback<MessageItem>(){

    override fun onZeroItemsLoaded() {

        RetrofitClient.getInstance().getApi().getMessages()
            .enqueue{
                onResponse={r->
                    if(r.isSuccessful)
                    repeat(r.body()!!.data.size){
                        db.myDao().insert(r.body()!!.data[it])
                    }

                }

                onFailure={


                }


            }
    }

    override fun onItemAtEndLoaded(itemAtEnd: MessageItem) {
        RetrofitClient.getInstance().getApi().getMessages(start = itemAtEnd.key!!.toInt())
            .enqueue{
                onResponse={r->
                    if(r.isSuccessful)
                    repeat(r.body()!!.data.size){
                        db.myDao().insert(r.body()!!.data[it])
                    }

                }

                onFailure={


                }


            }
    }

    override fun onItemAtFrontLoaded(itemAtFront: MessageItem) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}