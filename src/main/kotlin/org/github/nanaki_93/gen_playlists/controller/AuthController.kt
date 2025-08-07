package org.github.nanaki_93.gen_playlists.controller

import org.github.nanaki_93.gen_playlists.model.AuthenticationRequest
import org.github.nanaki_93.gen_playlists.model.AuthenticationResponse
import org.github.nanaki_93.gen_playlists.security.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService
) {
    @PostMapping("/authenticate")
    fun authenticate(@RequestBody request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val userDetails = userDetailsService.loadUserByUsername(request.email)
        val jwtToken = jwtService.generateToken(userDetails)
        return AuthenticationResponse(jwtToken)
    }
}

