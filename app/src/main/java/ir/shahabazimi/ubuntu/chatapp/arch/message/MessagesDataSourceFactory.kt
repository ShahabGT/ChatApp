package ir.shahabazimi.ubuntu.chatapp.arch.message

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource


class MessagesDataSourceFactory : DataSource.Factory<Int, MessageItem>() {

    private val itemLiveDataSource = MutableLiveData<PageKeyedDataSource<Int,MessageItem>>()

    private val messagesDataSource = MessageDataSource()

    override fun create(): DataSource<Int, MessageItem> {
        itemLiveDataSource.postValue(messagesDataSource)
        return messagesDataSource
    }

//    fun invalidateDatasource() {
//            messagesDataSource.isInvalid
//        create()
//    }

   // fun getItemLiveDataSource()= itemLiveDataSource

}