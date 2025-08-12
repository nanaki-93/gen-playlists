package org.github.nanaki_93.gen_playlists.controller


import org.github.nanaki_93.gen_playlists.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists.model.SpotifyAuthCode
import org.github.nanaki_93.gen_playlists.security.JwtService
import org.github.nanaki_93.gen_playlists.service.SpotifyAuthApi
import org.github.nanaki_93.gen_playlists.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val spotifyAuthApi: SpotifyAuthApi,
    private val jwtService: JwtService,
    private val userService: UserService,

    ) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/login")
    fun fromSpotify(@RequestBody spotifyCode: SpotifyAuthCode) =
        ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(spotifyAuthApi.loginOrSignup(spotifyCode));


    @PostMapping("/register")
    fun register(@RequestBody createUserDto: CreateUserDto) =
        ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.register(createUserDto))
}

