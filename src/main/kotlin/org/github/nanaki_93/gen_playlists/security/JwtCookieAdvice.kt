package org.github.nanaki_93.gen_playlists.security

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.github.nanaki_93.gen_playlists.dto.CreateUserDto
import org.github.nanaki_93.gen_playlists.dto.UserDto
import org.github.nanaki_93.gen_playlists.mapper.UserMapper
import org.github.nanaki_93.gen_playlists.model.User
import org.github.nanaki_93.gen_playlists.service.UserService
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
class JwtCookieAdvice(val jwtService: JwtService, val userService: UserService, val userMapper: UserMapper) :
    ResponseBodyAdvice<Any> {


    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val httpResponse = (response as? ServletServerHttpResponse)?.servletResponse
        val authentication = SecurityContextHolder.getContext().authentication

        // Prioritize handling auth endpoints
        if (httpResponse != null && isAuthEndpoint(request)) {
            handleAuthEndpoint(body, httpResponse)
        }
        // Handle authenticated users for other endpoints (if a User principal is present)
        else if (httpResponse != null && authentication != null && authentication.isAuthenticated && authentication.principal is User) {
            val jwtToken = generateJwtToken(authentication.principal as User)
            setJwtCookie(httpResponse, jwtToken)
        }

        return body
    }


    private fun isAuthEndpoint(request: ServerHttpRequest): Boolean {
        val path = request.uri.path
        return path.contains("/api/v1/auth/login") || path.contains("/api/v1/auth/register")
    }

    private fun handleAuthEndpoint(body: Any?, response: HttpServletResponse) {
        when (body) {
            is CreateUserDto -> {
                if (!body.toRegister) {
                    // Existing user logging in
                    val user = userService.getUserByEmail(body.email) ?: throw IllegalStateException("User not found")
                    val jwtToken = jwtService.generateToken(user)
                    setJwtCookie(response, jwtToken)
                }
            }

            is UserDto -> {
                // New user registered
                val user = userService.getUserByEmail(body.email) ?: throw IllegalStateException("User not found")
                val jwtToken = jwtService.generateToken(user)
                setJwtCookie(response, jwtToken)
            }
        }
    }

    private fun setJwtCookie(response: HttpServletResponse, jwtToken: String) {
        val cookie = Cookie("jwt", jwtToken).apply {
            isHttpOnly = true
            secure = false // Set to true in production
            path = "/"
            maxAge = 86400
        }
        response.addCookie(cookie)
    }

    private fun generateJwtToken(user: User): String {
        return jwtService.generateToken(user)
    }
}