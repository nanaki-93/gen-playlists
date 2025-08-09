package org.github.nanaki_93.gen_playlists.service

import org.github.nanaki_93.gen_playlists.config.ApplicationProperties
import org.github.nanaki_93.gen_playlists.model.SpotifyAuthCode
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestClient

@Service
class SpotifyApi(val appProps: ApplicationProperties) {

    private val client = RestClient.builder()
        .baseUrl("https://accounts.spotify.com")
        .build()

    fun getUser(spotifyAuthCode: SpotifyAuthCode): String {

        println("code: ${spotifyAuthCode.code}")
        val form: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("client_id", appProps.clientId)
            add("client_secret", appProps.clientSecret)
            add("grant_type", "authorization_code")
            add("code", spotifyAuthCode.code)
            add("redirect_uri", "http://localhost:3000")
        }

        return client.post()
            .uri("/api/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .body(form)
            .retrieve()
            .body(String::class.java)
            ?: error("Empty response from Spotify")
    }


}