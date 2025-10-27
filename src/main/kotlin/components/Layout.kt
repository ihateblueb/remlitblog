package site.remlit.components

import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.html
import kotlinx.html.img
import kotlinx.html.main
import kotlinx.html.p
import kotlinx.html.stream.createHTML
import kotlinx.html.styleLink
import kotlinx.html.title

fun layout() = createHTML().html {
	head {
		title { +"%TITLE%remlit blog" }
		styleLink("/static/index.css")
	}
	body {
		header {
			div {
				classes = setOf("plate")
				p { +"remlit blog" }
			}
			img { src = "/static/img/remlit_sleeping.png" }
		}
		main {
			+"%CONTENT%"
		}
		footer {
			p { +"Queried in %QUERY_MS%ms, rendered in %RENDER_MS%ms" }
			img { src = "/static/img/icon/pumpkin.png" }
		}
	}
}