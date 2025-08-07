package org.github.nanaki_93.gen_playlists

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<GenPlaylistsApplication>().with(TestcontainersConfiguration::class).run(*args)
}
