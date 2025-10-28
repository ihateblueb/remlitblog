package site.remlit.service

import site.remlit.model.Button

object ButtonService {
	fun getAll(): List<Button> {
		val list = this::class.java.getResource("/buttons/_manifest.txt")?.readText()?.lines() ?: emptyList()
		val buttons = mutableListOf<Button>()

		list.forEach {
			buttons.add(
				Button(it.split(":")[0], "https:/${it.split(":").getOrNull(1)}/")
			)
		}

		return buttons
	}
}