package org.github.nanaki_93.views

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.Route
import org.github.nanaki_93.client.SpotifyClient


@Route("callback")
class CallbackView : Div(), BeforeEnterObserver {
    private val spotifyClient = SpotifyClient()


    override fun beforeEnter(event: BeforeEnterEvent) {
        val location = event.location
        val queryParams = location.queryParameters

        val code = queryParams.parameters["code"]?.firstOrNull()
        val error = queryParams.parameters["error"]?.firstOrNull()


        when {
            error != null -> {
                add("Authorization failed: $error")
                println("Spotify authorization error: $error")
            }

            code != null -> {
                add("Authorization successful! Processing...")
                println("Received authorization code: $code")


                // Exchange the code for an access token

                spotifyClient.setAccessToken(code)


                // Redirect back to main page after a short delay
                UI.getCurrent().access {
                    UI.getCurrent().navigate("")
                }

            }

            else -> {
                add("No authorization code received")
            }
        }

    }
}