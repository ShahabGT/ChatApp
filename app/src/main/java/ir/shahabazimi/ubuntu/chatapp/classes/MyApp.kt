package ir.shahabazimi.ubuntu.chatapp.classes

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.FirebaseApp
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import androidx.core.provider.FontRequest
import ir.shahabazimi.ubuntu.chatapp.R





class MyApp:Application(){


    override fun onCreate() {
        super.onCreate()
        Fresco.initialize(this)
    //    FirebaseApp.initializeApp(this)

        val fontRequest = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            "Noto Color Emoji Compat",
            R.array.com_google_android_gms_fonts_certs
        )
        val config=FontRequestEmojiCompatConfig(this, fontRequest).setReplaceAll(true)
        EmojiCompat.init(config)
    }



    companion object{
        fun isActivityVisible(): Boolean {
            return activityVisible
        }

        fun activityResumed() {
            activityVisible = true
        }

        fun activityPaused() {
            activityVisible = false
        }

        private var activityVisible: Boolean = false
    }

}