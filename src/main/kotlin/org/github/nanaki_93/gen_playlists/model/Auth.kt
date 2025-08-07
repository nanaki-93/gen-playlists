package org.github.nanaki_93.gen_playlists.model

data class AuthenticationRequest(val email: String, val password: String)
data class AuthenticationResponse(val token: String)