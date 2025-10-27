package site.remlit

import com.ucasoft.ktor.simpleCache.SimpleCache
import com.ucasoft.ktor.simpleMemoryCache.*
import io.ktor.server.application.*
import kotlin.time.Duration.Companion.seconds

fun Application.configureHTTP() {
	install(SimpleCache) {
		memoryCache {
			invalidateAt = 5.seconds
		}
	}
}
