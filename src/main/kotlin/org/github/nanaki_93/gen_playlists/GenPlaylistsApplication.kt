package org.github.nanaki_93.gen_playlists

import org.github.nanaki_93.gen_playlists.config.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(ApplicationProperties::class)
@SpringBootApplication
class GenPlaylistsApplication

fun main(args: Array<String>) {
    runApplication<GenPlaylistsApplication>(*args)
}
