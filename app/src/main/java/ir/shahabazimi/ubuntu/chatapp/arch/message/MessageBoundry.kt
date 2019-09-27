package ir.shahabazimi.ubuntu.chatapp.arch.message

import androidx.paging.PagedList
import ir.shahabazimi.ubuntu.chatapp.data.RetrofitClient
import ir.shahabazimi.ubuntu.chatapp.enqueue
import ir.shahabazimi.ubuntu.chatapp.room.MyRoomDatabase

class MessageBoundry(private val db: MyRoomDatabase) : PagedList.BoundaryCallback<MessageItem>() {

    override fun onZeroItemsLoaded() {

        RetrofitClient.getInstance().getApi().getMessages()
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
        RetrofitClient.getInstance().getApi().getMessages(start = key)
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

    override fun onItemAtFrontLoaded(itemAtFront: MessageItem) {
        super.onItemAtFrontLoaded(itemAtFront)
    }
}