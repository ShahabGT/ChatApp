package ir.shahabazimi.ubuntu.chatapp.arch.message

import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import ir.shahabazimi.ubuntu.chatapp.room.MyDao
import ir.shahabazimi.ubuntu.chatapp.room.MyRoomDatabase


class MessagesViewModel(db:MyRoomDatabase) :ViewModel(){
    var itemPagedList: LiveData<PagedList<MessageItem>>? = null
    private var messagesDataSourceFactory: MessagesDataSourceFactory? = null
    init {
        messagesDataSourceFactory = MessagesDataSourceFactory()
        val config = (PagedList.Config.Builder())
            .setEnablePlaceholders(false)
            .setPageSize(10)
            .build()

        val b = MessageBoundry(db)

        //itemPagedList = LivePagedListBuilder(messagesDataSourceFactory!!,config).setBoundaryCallback(b).build()
        val dataSourceFactory = db.myDao().selectPaged()
        itemPagedList = LivePagedListBuilder(dataSourceFactory,config).setBoundaryCallback(b).build()
    }

    fun invalidateData() = messagesDataSourceFactory?.invalidateDatasource()

}