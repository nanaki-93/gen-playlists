package org.github.nanaki_93.client

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
import org.github.nanaki_93.util.Observable
import kotlin.coroutines.CoroutineContext


class SpotifyClient : CoroutineScope {

    var spotifyAccess: Observable<SpotifyAccess> = Observable()
    var spotifyArtist: Observable<String> = Observable()

    private val job = SupervisorJob()
    private val dispatcher = Dispatchers.Default
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher

    fun getAccessToken() {
        launch {
            spotifyAccess.value = HttpClient {
                install(ContentNegotiation) {
                    json()
                }
            }.post("https://accounts.spotify.com/api/token") {
                contentType(ContentType.Application.Json)
                headers {
                    append("Content-Type", "application/x-www-form-urlencoded")
                }
                setBody(FormDataContent(Parameters.build {
                    append("grant_type", "client_credentials")
                    append("client_id", SpotifyConfig.clientId)
                    append("client_secret", SpotifyConfig.clientSecret)
                }))
            }.body()
        }

    }

    fun getArtist(spotifyAccess: SpotifyAccess? = null) {
        launch {
            val client = HttpClient {
                install(Auth) {
                    bearer {
                        loadTokens {
                            spotifyAccess?.accessToken?.let { BearerTokens(accessToken = it, refreshToken = "") }
                        }
                    }
                }
            }

            val response = client.get("https://api.spotify.com/v1/artists/4Z8W4fKeB5YxbusRsdQVPb")

            println(response.body<String>())
            spotifyArtist.value = response.body<String>()
        }

    }
}