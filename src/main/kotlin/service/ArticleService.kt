package site.remlit.service

import site.remlit.model.Article

object ArticleService {
	fun getAll() {

	}

	fun get(id: String): Article? = try {
		Article(
			id,
			getTitle(id) ?: "",
			getDate(id) ?: "",
			this::class.java.getResource("/articles/$id.md")?.readText() ?: ""
		)
	} catch (_: Exception) {
		null
	}

	fun getMetadata() =
		this::class.java.getResource("/articles/_manifest.txt")?.readText()?.lines() ?: emptyList()

	fun getTitle(id: String): String? =
		getMetadata().firstOrNull { it.split(":").first() == id }
			?.split(":")?.getOrNull(1)

	fun getDate(id: String): String? =
		getMetadata().firstOrNull { it.split(":").first() == id }
			?.split(":")?.getOrNull(2)
}