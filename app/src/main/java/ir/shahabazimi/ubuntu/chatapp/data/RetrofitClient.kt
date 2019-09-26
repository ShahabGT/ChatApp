package ir.shahabazimi.ubuntu.chatapp.data

import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
import java.util.concurrent.TimeUnit


class RetrofitClient{
    private val baseUrl="https://shahabazimi.ir/chatapp/"
    private var retrofit: Retrofit

    init {
        var cipherSuites = ConnectionSpec.MODERN_TLS.cipherSuites()
        if (!cipherSuites!!.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
            cipherSuites = ArrayList(cipherSuites)
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
        }
        val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .cipherSuites(*cipherSuites.toTypedArray())
                .build()

        val client = OkHttpClient.Builder()
                .connectionSpecs(listOf(spec))
                .retryOnConnectionFailure(true)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .build()
        retrofit=Retrofit.Builder().baseUrl(baseUrl).client(client).addConverterFactory(
            GsonConverterFactory.create()).build()
    }

    companion object{
        private var instance:RetrofitClient?=null

        fun getInstance():RetrofitClient{
            if(instance==null)
                instance=RetrofitClient()
            return instance!!
        }
    }

    fun getApi(): Api {
        return retrofit.create(Api::class.java)
    }
}