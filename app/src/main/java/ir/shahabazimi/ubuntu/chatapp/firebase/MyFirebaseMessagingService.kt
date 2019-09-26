package ir.shahabazimi.ubuntu.chatapp.firebase

import Const
import MySharedPreference
import MyUtils
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Base64
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ir.shahabazimi.ubuntu.chatapp.MainActivity
import ir.shahabazimi.ubuntu.chatapp.R
import ir.shahabazimi.ubuntu.chatapp.arch.message.MessageItem
import ir.shahabazimi.ubuntu.chatapp.classes.MyApp
import ir.shahabazimi.ubuntu.chatapp.room.MyRoomDatabase
import java.text.SimpleDateFormat
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(m: RemoteMessage) {


        val data = m.data

        val date = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH).format(Date(m.sentTime))

        val item =
            MessageItem(data["rowid"]!!.toInt(), data["body"]!!, null, date, data["sender"]!!)

        MyRoomDatabase.getInstance(this)!!.myDao().insert(item)

        if (data["sender"]!! != MySharedPreference.getInstance(this).getUser()) {
            if (!MyApp.isActivityVisible()) {
                val text = Base64.decode(data["body"]!!, Base64.DEFAULT).toString(charset("UTF-8"))
                createNotification(data["sender"]!!, text)
            }

        }


    }

    override fun onNewToken(p0: String) {
        MySharedPreference.getInstance(this).setFBToken(p0)
        super.onNewToken(p0)
    }


    private fun createNotification(title: String, message: String) {

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
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        builder.setContentText(message)
        builder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
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