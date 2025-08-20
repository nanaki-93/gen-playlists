package org.github.nanaki_93.gen_playlists_be.controller


import org.github.nanaki_93.gen_playlists_be.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists_be.dto.UserDto
import org.github.nanaki_93.gen_playlists_be.model.SpotifyAuthCode
import org.github.nanaki_93.gen_playlists_be.service.SpotifyAuthApi
import org.github.nanaki_93.gen_playlists_be.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val spotifyAuthApi: SpotifyAuthApi,
    private val userService: UserService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping("/callback")
    fun callback() = ResponseEntity.status(HttpStatus.FOUND)

    @PostMapping("/login")
    fun fromSpotify(@RequestBody spotifyCode: SpotifyAuthCode) =
        ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(spotifyAuthApi.loginOrSignup(spotifyCode));


    @PostMapping("/register")
    fun register(@RequestBody createUserDto: CreateUserDto): ResponseEntity<UserDto> {
        logger.info("Registering user in Controller : $createUserDto")
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(userService.register(createUserDto))
    }
}

