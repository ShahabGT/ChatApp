package ir.shahabazimi.ubuntu.chatapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import ir.shahabazimi.ubuntu.chatapp.classes.MySharedPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(MySharedPreference.getInstance(this).getIsLogin()){
            startActivity(Intent(this,MainActivity::class.java))
            this.finish()
        }
        val playService = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (playService == ConnectionResult.SERVICE_MISSING
            || playService == ConnectionResult.SERVICE_MISSING_PERMISSION
            || playService == ConnectionResult.SERVICE_DISABLED
            || playService == ConnectionResult.SERVICE_INVALID
        ) {
            Toast.makeText(this, "Google Service is not available!", Toast.LENGTH_LONG).show()
            Handler().postDelayed({
                onBackPressed()

            }, 1500)

        } else if (playService == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
            Toast.makeText(this, "Update Play Services!", Toast.LENGTH_LONG).show()
            onBackPressed()
        }
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful)
                    return@OnCompleteListener

                val token = task.result?.token
                if (token.isNullOrBlank())
                    MySharedPreference.getInstance(this).setFBToken(token!!)
            })


    }


}

fun <T> Call<T>.enqueue(callback: CallBackKt<T>.() -> Unit) {
    val callBackKt = CallBackKt<T>()
    callback.invoke(callBackKt)
    this.enqueue(callBackKt)
}

class CallBackKt<T> : Callback<T> {

    var onResponse: ((Response<T>) -> Unit)? = null
    var onFailure: ((t: Throwable?) -> Unit)? = null

    override fun onFailure(call: Call<T>, t: Throwable) {
        onFailure?.invoke(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponse?.invoke(response)
    }

}

fun <T : View> Activity.bind(@IdRes id: Int): Lazy<T> =
    lazy {findViewById<T>(id) }
