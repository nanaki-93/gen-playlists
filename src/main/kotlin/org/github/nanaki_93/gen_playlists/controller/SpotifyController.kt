package org.github.nanaki_93.gen_playlists.controller

import org.github.nanaki_93.gen_playlists.service.SpotifyApi
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SpotifyController(private val spotifyApi: SpotifyApi) {

    @GetMapping("/playlists")
    fun getPlaylists(@RequestParam(name = "limit") limit: String, @RequestParam(name = "offset") offset: String) =
        ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(spotifyApi.retrieveSpotifyPlaylistList(limit, offset));

    @GetMapping("/playlist/{playlistId}")
    fun getPlaylistDetail(@PathVariable("playlistId") playlistId: String) =
        ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(spotifyApi.retrieveSpotifyPlaylistDetail(playlistId));
}