package org.github.nanaki_93.client

import com.vaadin.flow.component.UI
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.*
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.github.nanaki_93.config.SpotifyConfig
import org.github.nanaki_93.model.SpotifyAccess
import org.github.nanaki_93.model.SpotifyProfile
import java.net.URLEncoder
import kotlin.coroutines.CoroutineContext


class SpotifyClient : CoroutineScope {


    private val job = SupervisorJob()
    private val dispatcher = Dispatchers.Default
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher


    companion object {
        var spotifyAccess: SpotifyAccess? = null
        var spotifyProfile: SpotifyProfile? = null
    }

    fun login() {
        println("login called!")

        // Instead of making an HTTP request, redirect the user's browser
        val authUrl = "https://accounts.spotify.com/authorize?" +
                "client_id=${SpotifyConfig.clientId}" +
                "&response_type=code" +
                "&redirect_uri=${URLEncoder.encode("http://localhost:8080/callback", "UTF-8")}" +
                "&scope=${URLEncoder.encode("user-read-private user-read-email", "UTF-8")}"

        // Open the authorization URL in the user's browser
        UI.getCurrent().page.open(authUrl)
    }

    fun setAccessToken(authorizationCode: String) {
        launch {

            println("getAccessToken called!")

            spotifyAccess = HttpClient {
                install(ContentNegotiation) {
                    json()
                }
            }.post("https://accounts.spotify.com/api/token") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Content-Type", "application/x-www-form-urlencoded")
                }
                setBody(FormDataContent(Parameters.build {
                    append("grant_type", "authorization_code")
                    append("redirect_uri", "http://localhost:8080/callback")
                    append("client_id", SpotifyConfig.clientId)
                    append("client_secret", SpotifyConfig.clientSecret)
                    append("code", authorizationCode)
                }))
            }.body()

            println("access token: ${spotifyAccess?.accessToken}")
        }
    }


    fun setProfile(spotifyAccess: SpotifyAccess?) {

        launch {
            println("getProfile called! with access token: ${spotifyAccess?.accessToken}")
            val client = HttpClient {
                install(ContentNegotiation) {
                    json()
                }
                install(Auth) {
                    bearer {
                        loadTokens {
                            spotifyAccess?.accessToken?.let { BearerTokens(accessToken = it, refreshToken = "") }
                        }
                    }
                }
            }

            spotifyProfile = client.get("https://api.spotify.com/v1/me").body<SpotifyProfile>()
        }
    }
}