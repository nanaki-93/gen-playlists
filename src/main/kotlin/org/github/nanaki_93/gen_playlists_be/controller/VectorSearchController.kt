package org.github.nanaki_93.gen_playlists_be.controller

import org.github.nanaki_93.gen_playlists_be.dto.SongCharacteristicsRequest
import org.github.nanaki_93.gen_playlists_be.dto.SongRecommendationResponse
import org.github.nanaki_93.gen_playlists_be.service.VectorDbService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/vector-search")
class VectorSearchController(private val vectorDbService: VectorDbService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/recommend-songs")
    fun recommendSongs(
        @RequestBody request: SongCharacteristicsRequest,
        @RequestParam(name = "limit", defaultValue = "10") limit: Int
    ): ResponseEntity<List<SongRecommendationResponse>> {
        logger.info("Getting song recommendations: characteristics=${request.characteristics}, limit=$limit")

        val recommendations = vectorDbService.findSimilarSongs(request.characteristics, limit)

        return ResponseEntity.status(HttpStatus.OK).body(recommendations)
    }
}