package ir.shahabazimi.ubuntu.chatapp

import MySharedPreference
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.widget.EmojiEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessagesAdapter
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessagesViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessageItem
import ir.shahabazimi.ubuntu.chatapp.classes.MyApp
import ir.shahabazimi.ubuntu.chatapp.data.RetrofitClient
import ir.shahabazimi.ubuntu.chatapp.room.MyRoomDatabase


class MainActivity : AppCompatActivity() {

    private val message: EmojiEditText by bind(R.id.main_message, this)
    private val send: ImageView by bind(R.id.main_send, this)
    private val recycler: RecyclerView by bind(R.id.main_recycler, this)
    private val db = MyRoomDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        onClicks()
        showdata()
    }

    override fun onResume() {
        super.onResume()
        MyApp.activityResumed()
        MyUtils.removeNotification(this);
    }

    override fun onPause() {
        MyApp.activityPaused()
        super.onPause()
    }

    private fun init(){

        val layoutManager=LinearLayoutManager(this)
        layoutManager.reverseLayout=true
        layoutManager.stackFromEnd=true
        recycler.layoutManager=layoutManager
        recycler.scrollToPosition(0)
        recycler.addOnLayoutChangeListener{
            _,_,_,_,b,_,_,_,ob->
            if(b>ob)
                recycler.run {
                    recycler.scrollToPosition(0)
                }
        }
    }

    private fun showdata(){
        val db=MyRoomDatabase.getInstance(this)

        val viewModel=ViewModelProviders.of(this, object :ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T=MessagesViewModel(db!!) as T
        })[MessagesViewModel::class.java]
        val adapter=MessagesAdapter(this)
        viewModel.itemPagedList!!.observe(this@MainActivity,Observer<PagedList<MessageItem>>{
            adapter.submitList(it)
        })
        recycler.adapter=adapter

    }

    private fun onClicks() {
        send.setOnClickListener {
            val user = MySharedPreference.getInstance(this).getUser()
            val body = Base64.encodeToString(message.text.toString().trim().toByteArray(charset("UTF-8")), Base64.DEFAULT)
            if (body.isNotBlank()) {
                message.setText("")
                RetrofitClient.getInstance().getApi().sendMessage(user, body)
                    .enqueue {
                        onResponse = {
                            if(it.isSuccessful){
                                db.getInstance(this@MainActivity)!!.myDao().insert(MessageItem(it.body()!!.id,body,null,it.body()!!.date,user))
                                recycler.scrollToPosition(0)
                                }
                        }
                        onFailure = {
                        }

                    }
            }
        }
    }
}
