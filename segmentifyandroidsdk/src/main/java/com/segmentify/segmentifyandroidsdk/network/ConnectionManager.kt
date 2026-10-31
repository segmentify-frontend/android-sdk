package com.segmentify.segmentifyandroidsdk.network

import android.content.Context
import android.net.ConnectivityManager
import com.segmentify.segmentifyandroidsdk.SegmentifyManager
import com.segmentify.segmentifyandroidsdk.network.Factories.EventFactory
import com.segmentify.segmentifyandroidsdk.network.Factories.PushFactory
import com.segmentify.segmentifyandroidsdk.network.Factories.UserSessionFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
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
    // Shared Gson for request bodies (fills userId/sessionId of SegmentifyObject)
    internal val gson: Gson = GsonBuilder()
            .registerTypeAdapterFactory(SegmentifyObjectAdapterFactory())
            .create()

    init {
        updateLoggingLevel()

        val httpClient = OkHttpClient.Builder()

        // Add logging interceptor before the auth interceptor so that
        // the Authorization header is not captured in logs.
        httpClient.addInterceptor(logging)

        // Common headers + auth: Basic Authorization header if authHeader is set, otherwise apiKey query param
        httpClient.addInterceptor(AuthInterceptor { SegmentifyManager.configModel })
        httpClient.connectTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)
        httpClient.readTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)

        client = httpClient.build()
        val keyService = Retrofit.Builder()
                .baseUrl(SegmentifyManager.clientPreferences?.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

        userSessionFactory = keyService.create(UserSessionFactory::class.java)

        val eventService = Retrofit.Builder()
                .baseUrl(SegmentifyManager.clientPreferences?.getApiUrl())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()

        eventFactory = eventService.create(EventFactory::class.java)

        if (SegmentifyManager.configModel?.dataCenterUrlPush != null) {
            val pushService = Retrofit.Builder()
                    .baseUrl(SegmentifyManager.configModel?.dataCenterUrlPush)
                    .addConverterFactory(GsonConverterFactory.create(gson))
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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
        eventFactory = eventService.create(EventFactory::class.java)

        if (SegmentifyManager.configModel?.dataCenterUrlPush != null) {
            val pushService = Retrofit.Builder()
                    .baseUrl(SegmentifyManager.configModel?.dataCenterUrlPush)
                    .addConverterFactory(GsonConverterFactory.create(gson))
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
