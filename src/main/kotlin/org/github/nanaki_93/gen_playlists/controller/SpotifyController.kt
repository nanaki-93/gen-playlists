package org.github.nanaki_93.gen_playlists.controller

import org.github.nanaki_93.gen_playlists.model.SpotifyPageRes
import org.github.nanaki_93.gen_playlists.model.SpotifyPlaylistRes
import org.github.nanaki_93.gen_playlists.service.SpotifyApi
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gen-playlists")
class SpotifyController(private val spotifyApi: SpotifyApi) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/playlists")
    fun getPlaylists(
        @RequestParam(name = "limit") limit: String, @RequestParam(name = "offset") offset: String
    ): ResponseEntity<SpotifyPageRes<SpotifyPlaylistRes>> {
        logger.info("Getting playlists : limit=$limit, offset=$offset")
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(spotifyApi.retrieveSpotifyPlaylistList(limit, offset));
    }

    @GetMapping("/playlist/{playlistId}")
    fun getPlaylistDetail(@PathVariable("playlistId") playlistId: String) =
        ResponseEntity.status(HttpStatus.ACCEPTED).body(spotifyApi.retrieveSpotifyPlaylistDetail(playlistId));
}