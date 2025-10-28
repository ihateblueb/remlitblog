package site.remlit

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import site.remlit.route.article
import site.remlit.route.index

fun Application.configureRouting() {
	routing {
		staticResources("/static", "static")

		index()
		article()
	}
}
