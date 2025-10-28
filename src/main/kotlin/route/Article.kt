package site.remlit.route

import io.ktor.http.HttpStatusCode
import io.ktor.http.invoke
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.span
import kotlinx.html.stream.createHTML
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import site.remlit.components.layout
import site.remlit.model.Article
import site.remlit.service.ArticleService
import kotlin.system.measureTimeMillis

fun Route.article() {
	get("/article/{id}") {
		val id = call.parameters.getOrFail("id")

		var article: Article
		val queryTime = measureTimeMillis {
			article = ArticleService.get(id)
				?: return@get call.respond(HttpStatusCode.NotFound)
		}

		if (article.title.isBlank() || article.date.isBlank() || article.content.isBlank())
			return@get call.respond(HttpStatusCode.NotFound)

		call.response.headers.append("Content-Type", "text/html;charset=utf-8")

		var html = ""

		val renderTime = measureTimeMillis {
			val flavor = GFMFlavourDescriptor()
			val parsed = MarkdownParser(flavor)
				.buildMarkdownTreeFromString(article.content)

			val headerHtml = createHTML().div {
				classes = setOf("articleTitle")
				div {
					h1 {
						+article.title
					}
					span {
						+"Written by Harper on ${article.date}"
					}
				}
			}

			val contentHtml = HtmlGenerator(article.content, parsed, flavor)
				.generateHtml()

			// todo: syntax highlighting

			html = layout()
				.replace("%QUERY_MS%", "$queryTime")
				.replace("%TITLE%", "${article.title} - ")
				.replace("%CONTENT%", "$headerHtml<article>$contentHtml</article>")
		}

		call.respond(html.replace("%RENDER_MS%", "$renderTime"))
	}
}