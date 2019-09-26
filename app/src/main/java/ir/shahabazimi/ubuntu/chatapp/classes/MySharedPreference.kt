
import android.content.Context
import android.content.SharedPreferences

class MySharedPreference private constructor(context: Context){
    private val sp:SharedPreferences=context.getSharedPreferences("RadicalPreference",0)
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

    fun getFirstBoot() = sp.getBoolean("firstBoot",true)
    fun setFirstBoot() = editor.putBoolean("firstBoot",false).apply()

    fun getFirstStat() = sp.getBoolean("FirstStat",true)
    fun setFirstStat() = editor.putBoolean("FirstStat",false).apply()

    fun getFirstProfile() =sp.getBoolean("FirstProfile",true)
    fun setFirstProfile() = editor.putBoolean("FirstProfile",false).apply()

    fun getFirstFavorite()= sp.getBoolean("FirstFavorite",true)
    fun setFirstFavorite() = editor.putBoolean("FirstFavorite",false).apply()

    fun getFirstSupport() = sp.getBoolean("FirstSupport",true)
    fun setFirstSupport() =  editor.putBoolean("FirstSupport",false).apply()

    fun getFirstAbout()=  sp.getBoolean("FirstAbout",true)
    fun setFirstAbout() = editor.putBoolean("FirstAbout",false).apply()

    fun getFirstSearch() = sp.getBoolean("FirstSearch",true)
    fun setFirstSearch() = editor.putBoolean("FirstSearch",false).apply()

    fun getFirstShare() = sp.getBoolean("FirstShare",true)
    fun setFirstShare() =  editor.putBoolean("FirstShare",false).apply()

    fun getFirstHelp() = sp.getBoolean("FirstHelp",true)
    fun setFirstHelp() = editor.putBoolean("FirstHelp",false).apply()

    fun getIsLogin() = sp.getBoolean("IsLogin",false)
    fun setIsLogin() = editor.putBoolean("IsLogin",true).apply();

    fun getFBToken() = sp.getString("FBToken","")
    fun setFBToken(FBToken:String) = editor.putString("FBToken",FBToken).apply()

    fun getAccessToken()= sp.getString("AccessToken","")
    fun setAccessToken(AccessToken:String) = editor.putString("AccessToken",AccessToken).apply()

    fun getUser()=sp.getString("User","")
    fun setUser(user:String)=editor.putString("User",user).apply()

    fun getSex() = sp.getString("sex","")
    fun setSex(sex:String)= editor.putString("sex",sex).apply()

    fun getEmail() = sp.getString("Email","")
    fun setEmail(Email:String) = editor.putString("Email",Email).apply()

    fun getName() = sp.getString("Name","")
    fun setName(Name:String)=editor.putString("Name",Name).apply()

    fun getBirthday() = sp.getString("Birthday","")
    fun setBirthday(Birthday:String)=editor.putString("Birthday",Birthday).apply()

    fun getFamily() = sp.getString("Family","")
    fun setFamily(Family:String)=editor.putString("Family",Family).apply()

    fun getInviteCode() = sp.getString("InviteCode","")
    fun setInviteCode(InviteCode:String)=editor.putString("InviteCode",InviteCode).apply()

    fun getPlan() = sp.getInt("Plan",1)
    fun setPlan(Plan:Int)=editor.putInt("Plan",Plan).apply()

    fun getNewVersion() = sp.getInt("NewVersion",1)
    fun setNewVersion(NewVersion:Int)=editor.putInt("NewVersion",NewVersion).apply()
}