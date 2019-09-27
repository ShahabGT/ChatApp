package ir.shahabazimi.ubuntu.chatapp.data

import ir.shahabazimi.ubuntu.chatapp.arch.message.MessageResponse
import ir.shahabazimi.ubuntu.chatapp.models.JsonResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface Api {

    @FormUrlEncoded
    @POST("login.php")
    fun  doLogin(
            @Field("username") username:String,
            @Field("password")  password:String,
            @Field("token")  token:String
    ):Call<JsonResponse>
    //___________________________________________

    @FormUrlEncoded
    @POST("register.php")
    fun doRegister(
            @Field("name")  name:String,
            @Field("username")  username:String,
            @Field("email")  email:String,
            @Field("password")  password:String,
            @Field("token")  token:String,
            @Field("image")  image:String
    ):Call<JsonResponse>
    //___________________________________________


    @FormUrlEncoded
    @POST("sendmessage.php")
    fun sendMessage(
            @Field("username")  username:String?,
            @Field("message")  message:String
    ):Call<JsonResponse>
    //___________________________________________

    @GET("getmessages.php")
     fun getMessages(
            @Query("start") start:Int=0,
            @Query("size")  size:Int=10
    ):Call<MessageResponse>

    //___________________________________________

}
