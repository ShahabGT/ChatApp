package ir.shahabazimi.ubuntu.chatapp.classes

import android.content.Context
import android.content.SharedPreferences

class MySharedPreference private constructor(context: Context){
    private val sp:SharedPreferences=context.getSharedPreferences("ChatAppPreference",0)
    private val editor:SharedPreferences.Editor = sp.edit()

    companion object {
        private var instance:MySharedPreference?=null

        fun getInstance(context: Context):MySharedPreference{
            if(instance==null)
                instance=MySharedPreference(context)
            return instance!!
        }
    }

    fun clear()=sp.edit().clear().apply()

    fun getCameraPermission() = sp.getBoolean("camera",false)
    fun setCameraPermission() = editor.putBoolean("camera",true).apply()

    fun getIsLogin() = sp.getBoolean("IsLogin",false)
    fun setIsLogin() = editor.putBoolean("IsLogin",true).apply()

    fun getFBToken() = sp.getString("FBToken","")
    fun setFBToken(FBToken:String) = editor.putString("FBToken",FBToken).apply()

    fun getAccessToken()= sp.getString("AccessToken","")
    fun setAccessToken(AccessToken:String) = editor.putString("AccessToken",AccessToken).apply()

    fun getUser()=sp.getString("User","")
    fun setUser(user:String)=editor.putString("User",user).apply()

    fun getEmail() = sp.getString("Email","")
    fun setEmail(Email:String) = editor.putString("Email",Email).apply()

    fun getName() = sp.getString("Name","")
    fun setName(Name:String)=editor.putString("Name",Name).apply()


    fun getNewVersion() = sp.getInt("NewVersion",1)
    fun setNewVersion(NewVersion:Int)=editor.putInt("NewVersion",NewVersion).apply()
}