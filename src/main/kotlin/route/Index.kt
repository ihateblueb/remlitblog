package site.remlit.route

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.html.a
import kotlinx.html.b
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import site.remlit.components.layout
import site.remlit.service.ArticleService
import kotlin.system.measureTimeMillis

fun Route.index() {
	get("/") {
		var articles: List<String>
		val queryTime = measureTimeMillis {
			articles = ArticleService.getAll()
		}

		call.response.headers.append("Content-Type", "text/html;charset=utf-8")

		var html = ""

		val renderTime = measureTimeMillis {
			val headerHtml = createHTML().div {
				classes = setOf("articleTitle")
				div {
					h1 {
						+"Articles"
					}
					span {
						+"${articles.size} total"
					}
				}
			}

			var renderedArticles = "<ul>"
			for (article in articles) {
				val split = article.split(":")

				renderedArticles += createHTML().li {
					a {
						href = "/article/${split[0]}"
						b { +split[1] }
						p { +"Written by Harper on ${split[2]}" }
					}
				}
			}
			renderedArticles += "</ul>"

			html = layout()
				.replace("%QUERY_MS%", "$queryTime")
				.replace("%TITLE%", "")
				.replace("%CONTENT%", "$headerHtml$renderedArticles")
		}

		call.respond(html.replace("%RENDER_MS%", "$renderTime"))
	}
}