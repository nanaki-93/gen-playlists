package org.github.nanaki_93.views

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasElement
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.router.RouterLayout
import com.vaadin.flow.theme.lumo.Lumo


class MainLayout : KComposite(), RouterLayout {
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
        getElement().executeJs("document.body.classList.toggle('dark-theme');")
    }


    override fun showRouterLayoutContent(content: HasElement) {
        root.add(content as Component)
        content.isExpand = true
    }
}