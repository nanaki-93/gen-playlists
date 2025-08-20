package org.github.nanaki_93.gen_playlists_be

import org.github.nanaki_93.gen_playlists_be.config.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(ApplicationProperties::class)
@SpringBootApplication // Adjust to your base package
class GenPlaylistsApplication

fun main(args: Array<String>) {
    runApplication<GenPlaylistsApplication>(*args)
}
