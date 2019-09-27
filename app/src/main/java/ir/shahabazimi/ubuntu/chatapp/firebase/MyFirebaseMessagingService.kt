package ir.shahabazimi.ubuntu.chatapp.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Base64
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.shahabazimi.ubuntu.chatapp.MainActivity
import ir.shahabazimi.ubuntu.chatapp.R
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessageItem
import ir.shahabazimi.ubuntu.chatapp.classes.Const
import ir.shahabazimi.ubuntu.chatapp.classes.MyApp
import ir.shahabazimi.ubuntu.chatapp.classes.MySharedPreference
import ir.shahabazimi.ubuntu.chatapp.room.MyRoomDatabase
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(m: RemoteMessage) {


        val data = m.data

        val item =
            MessageItem(data["rowid"]!!.toInt(), data["body"]!!, null, data["date"]!!, data["sender"]!!)

        MyRoomDatabase.getInstance(this)!!.myDao().insert(item)

        if (data["sender"]!! != MySharedPreference.getInstance(this).getUser()) {
            if (!MyApp.isActivityVisible()) {
                val text = Base64.decode(data["body"]!!, Base64.DEFAULT).toString(charset("UTF-8"))


                downloadAvatar(data["sender"]!!, text,data["sender"]!!)
            }

        }

    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        MySharedPreference.getInstance(this).setFBToken(p0)
    }

    private fun downloadAvatar(title: String, message: String,username:String){
        val imageRequest = ImageRequest.fromUri("https://radical-app.ir/chatapp/avatars/${username.toLowerCase(Locale.ENGLISH)}.jpg")
        val imagePipeline = Fresco.getImagePipeline()
        val dataSource = imagePipeline.fetchDecodedImage(imageRequest, null)
        dataSource.subscribe(object :BaseBitmapDataSubscriber(){
            override fun onNewResultImpl(bitmap: Bitmap?) {
                createNotification(title,message,bitmap)
            }

            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                createNotification(title,message,null)

            }

        },UiThreadImmediateExecutorService.getInstance())
    }


    private fun createNotification(title: String, message: String,bitmap: Bitmap?) {

        val intent = Intent(this, MainActivity::class.java)
        //intent.putExtra("notidata", intentValue)
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        // val alarmSound = Uri.parse("android.resource://" + packageName + "/" + R.raw.notification)
        createNotificationChannel()
        val builder = NotificationCompat.Builder(this, Const.CHANNEL_CODE)

        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle(title)
        if(bitmap==null)
            builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.avatar))
        else
            builder.setLargeIcon(bitmap)
        builder.setContentText(message)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        builder.priority=NotificationCompat.PRIORITY_HIGH
        builder.setAutoCancel(true)
        builder.setContentIntent(pendingIntent)
        // builder.setSound(alarmSound, AudioManager.STREAM_NOTIFICATION)
        builder.setVibrate(longArrayOf(1000, 1000))
        builder.setLights(Color.YELLOW, 1000, 1000)
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(Const.NOTIFICATION_ID, builder.build())


    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ChatApp Message Notifications"
            val description = "Using this channel to display notification for chat app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(Const.CHANNEL_CODE, name, importance)
            notificationChannel.description = description
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


}