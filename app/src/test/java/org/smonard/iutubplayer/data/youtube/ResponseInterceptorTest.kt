package org.smonard.iutubplayer.data.youtube

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.http.RealResponseBody
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.core.Is
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.smonard.iutubplayer.helpers.TestUtils
import org.smonard.iutubplayer.util.OutOfQuotaException
import org.smonard.iutubplayer.util.YoutubeApiException
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class ResponseInterceptorTest {
    @InjectMocks
    private lateinit var responseInterceptor: ResponseInterceptor
    @Mock
    private val chain: Interceptor.Chain? = null
    private var request: Request? = null
    @Before
    fun setUp() {
        request = Request.Builder().url("http://localhost/").build()
        Mockito.`when`(chain!!.request()).thenReturn(request)
    }

    @Test
    @Throws(IOException::class)
    fun returnsActualResponseIfBceOk() {
        val expectedResponse =
            Response.Builder().code(200).protocol(Protocol.HTTP_1_1).message("ok")
                .request(request!!).build()
        Mockito.`when`(chain!!.proceed(TestUtils.eq(request)!!))
            .thenReturn(expectedResponse)
        val response = responseInterceptor.intercept(chain)
        Assert.assertThat(
            response,
            Is.`is`(expectedResponse)
        )
    }

    @Test(expected = OutOfQuotaException::class)
    @Throws(IOException::class)
    fun throwsOutOfQuotaExceptionIfErrorCodeEquals403() {
        val body = "{\"error\":{\"code\":403,\"message\":\"error\"}}"
        val expectedResponse =
            Response.Builder().message("403").protocol(Protocol.HTTP_1_1)
                .request(request!!).code(403)
                .body(
                    RealResponseBody(
                        "application/json",
                        128,
                        MockResponse().setBody(body).getBody()!!
                    )
                )
                .build()
        Mockito.`when`(chain!!.proceed(TestUtils.eq(request)!!))
            .thenReturn(expectedResponse)
        responseInterceptor.intercept(chain)
    }

    @get:Rule
    var expectedException = ExpectedException.none()

    @Test
    @Throws(IOException::class)
    fun throwsYoutubeExceptionWhitMessageWhenAvailable() {
        val body = "{\"error\":{\"code\":499,\"message\":\"error\"}}"
        val expectedResponse =
            Response.Builder().message("403").protocol(Protocol.HTTP_1_1)
                .request(request!!).code(403)
                .body(
                    RealResponseBody(
                        "application/json",
                        128,
                        MockResponse().setBody(body).getBody()!!
                    )
                )
                .build()
        Mockito.`when`(chain!!.proceed(TestUtils.eq(request)!!))
            .thenReturn(expectedResponse)
        expectedException.expect(YoutubeApiException::class.java)
        expectedException.expectMessage("Unknown error at Youtube API. error")
        responseInterceptor.intercept(chain)
    }

    @Test
    @Throws(IOException::class)
    fun throwsYoutubeExceptionIfUnknownErrorCode() {
        val body = "{\"errora\":{\"code\":499,\"messaga\":\"error\"}}"
        val expectedResponse =
            Response.Builder().message("403").protocol(Protocol.HTTP_1_1)
                .request(request!!).code(403)
                .body(
                    RealResponseBody(
                        "application/json",
                        128,
                        MockResponse().setBody(body).getBody()!!
                    )
                )
                .build()
        Mockito.`when`(chain!!.proceed(TestUtils.eq(request)!!))
            .thenReturn(expectedResponse)
        expectedException.expect(YoutubeApiException::class.java)
        expectedException.expectMessage("Unknown error at Youtube API. ")
        responseInterceptor.intercept(chain)
    }
}