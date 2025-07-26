package org.github.nanaki_93.views

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.RouterLayout
import org.github.nanaki_93.client.SpotifyClient


class MainLayout : KComposite(), RouterLayout {
    private val spotifyClient: SpotifyClient = SpotifyClient()
    private val root = ui {
        verticalLayout {
            horizontalLayout {
                setWidthFull()
                div { h1("TODO") }
                button(icon = VaadinIcon.MOON.create()) {
                    onClick { toggleTheme() }
                    element.style.set("margin-left", "auto")
                }
            }
            content {
                align(center, top)
            }
        }
    }

    private fun toggleTheme() {
        spotifyClient.getArtist(spotifyClient.spotifyAccess.value)
    }


    override fun showRouterLayoutContent(content: HasElement) {
        root.add(content as Component)
        content.isExpand = true
    }

    init {
        configObservers()
        spotifyClient.getAccessToken()
    }

    private fun configObservers() {

    }
}