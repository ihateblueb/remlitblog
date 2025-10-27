package site.remlit

import com.ucasoft.ktor.simpleCache.SimpleCache
import com.ucasoft.ktor.simpleCache.cacheOutput
import com.ucasoft.ktor.simpleMemoryCache.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlinx.html.*
import org.slf4j.event.*
import site.remlit.route.article
import site.remlit.route.index

fun Application.configureRouting() {
	routing {
		staticResources("/static", "static")

		index()
		article()
	}
}
