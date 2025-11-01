package site.remlit.service

import site.remlit.model.Button

object ButtonService {
	fun getAll(): List<Button> {
		val list = this::class.java.getResource("/buttons/_manifest.txt")?.readText()?.lines() ?: emptyList()
		val buttons = mutableListOf<Button>()

		list.forEach {
			val url = it.split(":").getOrNull(1)
			buttons.add(
				Button(it.split(":")[0], if (url != null) "https://${url}/" else null)
			)
		}

		return buttons
	}
}