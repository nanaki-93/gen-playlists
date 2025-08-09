package org.github.nanaki_93.gen_playlists.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
data class ApplicationProperties(val clientId: String, val clientSecret: String)