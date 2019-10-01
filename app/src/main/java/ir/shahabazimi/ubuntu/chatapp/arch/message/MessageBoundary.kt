package ir.shahabazimi.ubuntu.chatapp.arch.message

import androidx.paging.PagedList
import ir.shahabazimi.ubuntu.chatapp.data.RetrofitClient
import ir.shahabazimi.ubuntu.chatapp.enqueue
import ir.shahabazimi.ubuntu.chatapp.room.MyRoomDatabase

class MessageBoundary(private val db: MyRoomDatabase) : PagedList.BoundaryCallback<MessageItem>() {

    override fun onZeroItemsLoaded() {

        RetrofitClient.instance.getApi().getMessages()
            .enqueue {
                onResponse = { r ->
                    if (r.isSuccessful)
                        repeat(r.body()!!.data.size) {
                            db.myDao().insert(r.body()!!.data[it])
                        }

                }

                onFailure = {


                }


            }
    }

    override fun onItemAtEndLoaded(itemAtEnd: MessageItem) {
        var key = 0
        if (itemAtEnd.key != null)
            key = itemAtEnd.key.toInt()

        RetrofitClient.instance.getApi().getMessages(start = key)
            .enqueue {
                onResponse = { r ->
                    if (r.isSuccessful)
                        repeat(r.body()!!.data.size) {
                            db.myDao().insert(r.body()!!.data[it])
                        }

                }

                onFailure = {


                }


            }
    }
}