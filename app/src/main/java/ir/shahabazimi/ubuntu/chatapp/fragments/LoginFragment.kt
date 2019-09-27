package ir.shahabazimi.ubuntu.chatapp.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import ir.shahabazimi.ubuntu.chatapp.R
import ir.shahabazimi.ubuntu.chatapp.classes.MySharedPreference
import ir.shahabazimi.ubuntu.chatapp.classes.MyUtils
import ir.shahabazimi.ubuntu.chatapp.data.RetrofitClient
import ir.shahabazimi.ubuntu.chatapp.enqueue

class LoginFragment : Fragment() {

    private lateinit var v: View

    private lateinit var loginBtn:MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_login, container, false)

        init()

        return v
    }

    @Suppress("DEPRECATION")
    private fun init() {
        loginBtn = v.findViewById(R.id.login_login)
        v.findViewById<MaterialButton>(R.id.login_register).setOnClickListener {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        loginBtn.setOnClickListener {
            val u = v.findViewById<EditText>(R.id.login_username).text.toString()
            val p = v.findViewById<EditText>(R.id.login_password).text.toString()
            if (u.isBlank() || p.isBlank())
                Toast.makeText(context, "enter username/password", Toast.LENGTH_SHORT).show()
            else {
                if (!MyUtils.isInternetAvailable(context!!))
                    Toast.makeText(context, "no internet", Toast.LENGTH_SHORT).show()
                else {
                    var token: String = MySharedPreference.getInstance(context!!).getFBToken()!!
                    if (token.isBlank())
                        token = FirebaseInstanceId.getInstance().token!!
                    loginBtn.isEnabled=false
                    login(u, p, token)
                    FirebaseMessaging.getInstance().subscribeToTopic("chatapp_1")



                }
            }

        }


    }

    private fun login(username: String, password: String, token: String) {
        MyUtils.hideKeyboard(activity!!)
        RetrofitClient.getInstance().getApi()
            .doLogin(username, password, token)
            .enqueue {
                onResponse = {
                    loginBtn.isEnabled=true

                    if (it.isSuccessful) {

                        MySharedPreference.getInstance(context!!).setUser(username)
                        MySharedPreference.getInstance(context!!)
                            .setAccessToken(it.body()?.accessToken!!)
                        MySharedPreference.getInstance(context!!).setIsLogin()
                        FirebaseMessaging.getInstance().subscribeToTopic("chatapp_users")
                        Navigation.findNavController(v)
                            .navigate(R.id.action_loginFragment_to_mainActivity)
                        activity?.finish()


                    } else {

                        Toast.makeText(context,when(it.code()){
                            400->"user not found"
                            406->"wrong password"
                            else->"error! try again"
                        },Toast.LENGTH_LONG).show()

                    }
                }

                onFailure = {
                    loginBtn.isEnabled=true
                    Toast.makeText(context, "error! try again", Toast.LENGTH_LONG).show()
                }


            }


    }


}
