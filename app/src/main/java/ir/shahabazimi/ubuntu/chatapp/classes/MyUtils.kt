package ir.shahabazimi.ubuntu.chatapp.classes

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.inputmethod.InputMethodManager
import java.lang.Exception
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class MyUtils {

    companion object{

        @Suppress("DEPRECATION")
        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm?.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        result = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm?.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            result = true
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            result = true
                        }
                    }
                }
            }
            return result
        }

        fun hideKeyboard(activity: Activity){
                val inptMng : InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            try {
                inptMng.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }catch(e:Exception){}
        }

//        fun shareCode(activity: Activity,title:String){
//            val intent= Intent(Intent.ACTION_SEND)
//            intent.type="text/plain"
//            intent.putExtra(Intent.EXTRA_TEXT,title)
//            activity.startActivity(Intent.createChooser(intent,activity.getString(R.string.txt_share)))
//
//        }
        fun removeNotification(context: Context){
            val notificationManager:NotificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(Const.NOTIFICATION_ID)


        }

        fun isEmailValid(Email: String): Boolean {
            var email = Email
            val pattern: Pattern
            val res: Boolean
            val matcher: Matcher
            val emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
            pattern = Pattern.compile(emailPattern)
            matcher = pattern.matcher(email)
            if (matcher.matches()) {
                email = email.toLowerCase(Locale.ENGLISH)
                res = email.contains("@yahoo.") || email.contains("@gmail.") || email.contains("@aol.") || email.contains("@hotmail.") || email.contains("@ymail.") || email.contains("@live.")
            } else {
                res = false
            }
            return res
        }

    }


}