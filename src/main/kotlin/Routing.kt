package site.remlit

import com.ucasoft.ktor.simpleCache.cacheOutput
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import site.remlit.route.article
import site.remlit.route.index
import kotlin.time.Duration.Companion.hours

fun Application.configureRouting() {
	routing {
		cacheOutput(12.hours) {
			staticResources("/static", "static")
		}

		index()
		article()
	}
}
