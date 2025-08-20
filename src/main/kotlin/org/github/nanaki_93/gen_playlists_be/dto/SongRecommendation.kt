package org.github.nanaki_93.gen_playlists_be.dto


data class SongCharacteristicsRequest(
    val characteristics: Map<String, Double>
)


data class SongRecommendationResponse(
    val songId: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val similarityScore: Double,
    val characteristics: Map<String, Double>
)