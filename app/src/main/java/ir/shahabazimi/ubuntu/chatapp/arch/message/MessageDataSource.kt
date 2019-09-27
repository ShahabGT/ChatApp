package ir.shahabazimi.ubuntu.chatapp.arch.message

import androidx.paging.PageKeyedDataSource
import ir.shahabazimi.ubuntu.chatapp.data.RetrofitClient
import ir.shahabazimi.ubuntu.chatapp.enqueue

class MessageDataSource : PageKeyedDataSource<Int,MessageItem>(){

    private val size=50
    private val start=0

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MessageItem>
    ) {
        RetrofitClient.getInstance().getApi()
            .getMessages( params.requestedLoadSize)
            .enqueue{
                onResponse={
                    if(it.isSuccessful && it.body()!=null)
                        callback.onResult(it.body()!!.data,null,start+size)
                }
                onFailure={

                }

            }



    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MessageItem>) {
    RetrofitClient.getInstance().getApi()
        .getMessages(start=params.key)
        .enqueue{
            onResponse={
                val key = params.key+size
                if(it.isSuccessful && it.body()!=null)
                callback.onResult(it.body()!!.data,key)

            }
            onFailure={

            }

        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MessageItem>) {
    }

}