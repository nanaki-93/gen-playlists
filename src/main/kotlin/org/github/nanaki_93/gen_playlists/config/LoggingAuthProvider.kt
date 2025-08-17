package org.github.nanaki_93.gen_playlists.config

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException

// This class will wrap your DaoAuthenticationProvider to add logging
class LoggingAuthProvider(private val delegate: AuthenticationProvider) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(LoggingAuthProvider::class.java)

    override fun authenticate(authentication: Authentication): Authentication? {
        logger.info("Attempting to authenticate user: '${authentication.name}' using AuthenticationProvider.")
        try {
            val result = delegate.authenticate(authentication)
            if (result != null && result.isAuthenticated) {
                logger.info("Successfully authenticated user: '${authentication.name}'.")
            } else {
                logger.warn("Authentication failed for user: '${authentication.name}'. Result was null or not authenticated.")
            }
            return result
        } catch (e: AuthenticationException) {
            logger.error("Authentication failed for user: '${authentication.name}' with exception: ${e.message}")
            throw e // Re-throw the exception so Spring Security can handle it
        }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return delegate.supports(authentication)
    }
}