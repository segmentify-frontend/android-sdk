package com.segmentify.segmentifyandroidsdk.network

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.segmentify.segmentifyandroidsdk.SegmentifyManager
import com.segmentify.segmentifyandroidsdk.network.Factories.EventFactory
import com.segmentify.segmentifyandroidsdk.network.Factories.PushFactory
import com.segmentify.segmentifyandroidsdk.network.Factories.UserSessionFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ConnectionManager {
    private val timeoutInterval = 60
    private var userSessionFactory: UserSessionFactory
    private var eventFactory: EventFactory
    private lateinit var pushFactory: PushFactory
    private val client: OkHttpClient
    private val logging = HttpLoggingInterceptor()

    init {
        updateLoggingLevel()

        val httpClient = OkHttpClient.Builder()

        //Gelen response kontrol edilecek
        httpClient.addInterceptor(Interceptor { chain ->
            val request = chain?.request()
            var newRequestBuilder = request?.newBuilder()

            try {
                newRequestBuilder?.addHeader("Origin", SegmentifyManager.configModel.subDomain ?: "")
                        ?.addHeader("Content-Type", "application/json")
                        ?.addHeader("Accept", "application/json")

                if (!SegmentifyManager.configModel.authHeader.isNullOrEmpty()) {
                    newRequestBuilder?.addHeader("Authorization", SegmentifyManager.configModel.authHeader!!)
                } else if (!SegmentifyManager.configModel.apiKey.isNullOrEmpty()) {
                    val url = request?.url()?.newBuilder()
                            ?.addQueryParameter("apiKey", SegmentifyManager.configModel.apiKey)
                            ?.build()
                    newRequestBuilder?.url(url!!)
                }
            } catch (e: Exception) {
                Log.d("addHeader", "Error")
                e.printStackTrace()
                return@Interceptor chain?.proceed(request)!!
            }

            chain.proceed(newRequestBuilder!!.build())
        })

        httpClient.addInterceptor(logging)
        httpClient.connectTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)

        client = httpClient.build()
        val keyService = Retrofit.Builder()
                .baseUrl(SegmentifyManager.clientPreferences?.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        userSessionFactory = keyService.create(UserSessionFactory::class.java)

        val eventService = Retrofit.Builder()
                .baseUrl(SegmentifyManager.clientPreferences?.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        eventFactory = eventService.create(EventFactory::class.java)

        if (SegmentifyManager.configModel?.dataCenterUrlPush != null) {
            val pushService = Retrofit.Builder()
                    .baseUrl(SegmentifyManager.configModel?.dataCenterUrlPush)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()

            pushFactory = pushService.create(PushFactory::class.java)
        }
    }

    fun updateLoggingLevel() {
        logging.level = if (SegmentifyManager.clientPreferences?.isLogVisible() == true) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    fun getUserSessionFactory(): UserSessionFactory {
        return userSessionFactory
    }

    fun getEventFactory(): EventFactory {
        return eventFactory
    }

    fun getPushFactory(): PushFactory {
        return pushFactory
    }

    fun rebuildServices() {
        val eventService = Retrofit.Builder()
                .baseUrl(SegmentifyManager.clientPreferences?.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        eventFactory = eventService.create(EventFactory::class.java)

        if (SegmentifyManager.configModel?.dataCenterUrlPush != null) {
            val pushService = Retrofit.Builder()
                    .baseUrl(SegmentifyManager.configModel?.dataCenterUrlPush)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            pushFactory = pushService.create(PushFactory::class.java)
        }
    }

    fun getSyncClient(): OkHttpClient {
        return client
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }
}