package org.github.nanaki_93.views

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.Route
import com.vaadin.flow.router.RouterLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.github.nanaki_93.client.SpotifyClient
import org.github.nanaki_93.model.SpotifyProfile


@Route("")
class MainLayout : KComposite(), RouterLayout, CoroutineScope {
    private val spotifyClient: SpotifyClient = SpotifyClient()
    private lateinit var profileDiv: Div

    // Add coroutine scope for UI operations
    private val job = SupervisorJob()
    override val coroutineContext = Dispatchers.Main + job

    private val root = ui {
        verticalLayout {
            horizontalLayout {
                setWidthFull()
                div { h1("Spotify Profile Manager") }
                button(icon = VaadinIcon.MOON.create()) {
                    onClick { loginButton() }
                    element.style.set("margin-left", "auto")
                }
                button(icon = VaadinIcon.SUN_RISE.create()) {
                    onClick { setProfile() }
                    element.style.set("margin-left", "auto")
                }

            }
            // Add profile display div
            profileDiv = div {
                h2("Spotify Profile")
                span("Not logged in")
                element.style.set("padding", "20px")
                element.style.set("border", "1px solid #ccc")
                element.style.set("border-radius", "8px")
                element.style.set("margin", "10px 0")
            }

            content {
                align(center, top)
            }
        }
    }

    private fun loginButton() {
        spotifyClient.login()
    }


    override fun showRouterLayoutContent(content: HasElement) {
        root.add(content as Component)
        content.isExpand = true
    }


    fun setProfile() {
        if (SpotifyClient.spotifyAccess != null) {
            spotifyClient.setProfile(SpotifyClient.spotifyAccess)
            println("setProfile called: ${SpotifyClient.spotifyProfile}:qq")
            if (SpotifyClient.spotifyProfile != null) {
                updateProfileDisplay(SpotifyClient.spotifyProfile)
            }
        }

    }


    private fun updateProfileDisplay(profile: SpotifyProfile?) {
        println("updateProfileDisplay called : $profile")
        profileDiv.removeAll()

        if (profile != null) {
            profileDiv.apply {
                h2("Spotify Profile")

                // Display profile image if available
                if (profile.images.isNotEmpty()) {
                    val image = Image(profile.images.first().url, "Profile Picture")
                    image.width = "100px"
                    image.height = "100px"
                    image.element.style.set("border-radius", "50%")
                    add(image)
                }

                // Profile information
                div {
                    h3(profile.displayName)
                    p("Email: ${profile.email}")
                    p("Country: ${profile.country}")
                    p("User ID: ${profile.id}")
                    p("Product: ${profile.product}")
                    p("Followers: ${profile.followers.total}")

                    // External links
                    div {
                        h4("Links:")
                        anchor(profile.externalUrls.spotify, "Spotify Profile") {
                            element.setAttribute("target", "_blank")
                        }
                    }
                }
            }
        } else {
            profileDiv.apply {
                h2("Spotify Profile")
                span("Not logged in or profile not loaded")
            }
        }
    }

    override fun onDetach(detachEvent: com.vaadin.flow.component.DetachEvent?) {
        super.onDetach(detachEvent)
        job.cancel() // Clean up coroutines when component is detached
    }


}