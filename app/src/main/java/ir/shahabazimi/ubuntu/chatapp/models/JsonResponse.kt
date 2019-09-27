package ir.shahabazimi.ubuntu.chatapp.models
import com.google.gson.annotations.SerializedName


data class JsonResponse (val message:String="",
                         val accessToken:String="",
                         val id:Int,
                         val date:String=""
                         )


