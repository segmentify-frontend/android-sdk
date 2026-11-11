package com.segmentify.segmentifyandroidsdk

import com.segmentify.segmentifyandroidsdk.model.ConfigModel
import com.segmentify.segmentifyandroidsdk.network.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AuthInterceptorTest {

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.enqueue(MockResponse().setBody("{}"))
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    private fun send(config: ConfigModel?): RecordedRequest {
        val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor { config })
                .build()
        val request = Request.Builder().url(server.url("/add/events/v1.json?instanceId=x")).build()
        client.newCall(request).execute().close()
        return server.takeRequest()
    }

    @Test
    fun authHeader_sendsAuthorizationAsIs_andNoApiKey() {
        val recorded = send(ConfigModel().apply {
            apiKey = "legacy-key"
            authHeader = "Basic dG9rZW4="
        })

        assertEquals("Basic dG9rZW4=", recorded.getHeader("Authorization"))
        assertNull(recorded.requestUrl.queryParameter("apiKey"))
        assertEquals("x", recorded.requestUrl.queryParameter("instanceId"))
    }

    @Test
    fun authHeaderWithoutScheme_isNotPrefixed() {
        val recorded = send(ConfigModel().apply { authHeader = "dG9rZW4=" })

        assertEquals("dG9rZW4=", recorded.getHeader("Authorization"))
    }

    @Test
    fun apiKeyOnly_sendsApiKeyQuery_andNoAuthorization() {
        val recorded = send(ConfigModel().apply { apiKey = "legacy-key" })

        assertEquals("legacy-key", recorded.requestUrl.queryParameter("apiKey"))
        assertNull(recorded.getHeader("Authorization"))
    }

    @Test
    fun blankAuthHeader_fallsBackToApiKey() {
        val recorded = send(ConfigModel().apply {
            apiKey = "legacy-key"
            authHeader = "  "
        })

        assertEquals("legacy-key", recorded.requestUrl.queryParameter("apiKey"))
        assertNull(recorded.getHeader("Authorization"))
    }

    @Test
    fun noConfig_sendsRequestWithoutAuth() {
        val recorded = send(null)

        assertNull(recorded.getHeader("Authorization"))
        assertNull(recorded.requestUrl.queryParameter("apiKey"))
        assertEquals("application/json", recorded.getHeader("Accept"))
    }

    @Test
    fun subDomain_isSentAsOrigin() {
        val recorded = send(ConfigModel().apply {
            apiKey = "legacy-key"
            subDomain = "example.com"
        })

        assertEquals("example.com", recorded.getHeader("Origin"))
        assertEquals("application/json", recorded.getHeader("Content-Type"))
    }

    @Test
    fun authorizationValue_returnsValueAsIs_orNullWhenBlank() {
        assertEquals("abc", AuthInterceptor.authorizationValue("abc"))
        assertEquals("Basic abc", AuthInterceptor.authorizationValue("Basic abc"))
        assertEquals("Bearer abc", AuthInterceptor.authorizationValue("Bearer abc"))
        assertNull(AuthInterceptor.authorizationValue(""))
        assertNull(AuthInterceptor.authorizationValue("   "))
        assertNull(AuthInterceptor.authorizationValue(null))
    }
}
