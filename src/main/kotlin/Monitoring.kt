package site.remlit

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.request.*

fun Application.configureMonitoring() {
	install(CallLogging) {
		filter { call -> !call.request.path().startsWith("/static") }
	}
}
