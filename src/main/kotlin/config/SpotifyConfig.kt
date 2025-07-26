package org.github.nanaki_93.config

import java.util.*

object SpotifyConfig {
    private val properties = Properties().apply {
        val inputStream = SpotifyConfig::class.java.getResourceAsStream("/application.properties")
            ?: throw IllegalStateException("application.properties not found")
        load(inputStream)
        inputStream.close()
    }

    val clientId: String = properties.getProperty("spotify.client.id")
        ?: throw IllegalStateException("spotify.client.id not found")

    val clientSecret: String = properties.getProperty("spotify.client.secret")
        ?: throw IllegalStateException("spotify.client.secret not found")
}