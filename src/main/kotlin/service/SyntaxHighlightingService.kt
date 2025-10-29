package site.remlit.service

object SyntaxHighlightingService {
	fun createCodeblockRegex(language: String) =
		Regex("<code class=\"language-$language\">([\\s\\S]*?)</code>")

	fun highlight(html: String): String {
		var html = html
		val kotlinBlocks = createCodeblockRegex("kotlin").findAll(html)
		val javaBlocks = createCodeblockRegex("java").findAll(html)

		kotlinBlocks.forEach {
			html = html.replace(it.value, highlightKotlin(it.value))
		}

		return html
	}

	fun highlightKotlin(value: String): String {
		var html = value

		val strings = Regex("\"([\\s\\S]*?)\"")

		strings.findAll(html).forEach {
			if (it.value.contains("\"language-kotlin\"")) return@forEach
			html = html.replace(it.value, "<span class=\"string\">${it.value}</span>")
		}

		val comment = Regex("//(.*)")

		comment.findAll(html).forEach {
			html = html.replace(it.value, "<span class=\"comment\">${it.value}</span>")
		}

		val t = Regex("<T>|\\sT>|\\(T\\)")

		t.findAll(html).forEach {
			html = html.replace(it.value, it.value.replace("T", "<span class=\"t\">T</span>"))
		}

		val boolean = Regex("true|false")

		boolean.findAll(html).forEach {
			html = html.replace(
				it.value,
				it.value
					.replace("true", "<span class=\"true\">true</span>")
					.replace("false", "<span class=\"false\">false</span>")
			)
		}

		val classObjectInterfaceRegex = Regex("class\\s([a-zA-Z]*)".let {
			var regex = it
			regex += "|data\\sclass\\s([a-zA-Z]*)"
			regex += "|open\\sclass\\s([a-zA-Z]*)"
			regex += "|abstract\\sclass\\s([a-zA-Z]*)"
			regex += "|sealed\\sclass\\s([a-zA-Z]*)"
			regex += "|enum\\sclass\\s([a-zA-Z]*)"
			regex += "|value\\sclass\\s([a-zA-Z]*)"

			regex += "|companion\\sobject"
			regex += "|object\\s([a-zA-Z]*)"
			regex += "|interface\\s([a-zA-Z]*)"

			regex
		})

		classObjectInterfaceRegex.findAll(html).forEach {
			html = html.replace(
				it.value,
				it.value
					.replace("data class", "<span class=\"class\">data class</span>")
					.replace("open class", "<span class=\"class\">open class</span>")
					.replace("abstract class", "<span class=\"class\">abstract class</span>")
					.replace("sealed class", "<span class=\"class\">sealed class</span>")
					.replace("enum class", "<span class=\"class\">enum class</span>")
					.replace("value class", "<span class=\"class\">value class</span>")
					.replace("class", "<span class=\"class\">class</span>")
					.replace("companion object", "<span class=\"class\">companion object</span>")
					.replace("object", "<span class=\"class\">object</span>")
					.replace("interface", "<span class=\"class\">interface</span>")
			)
		}

		val variables = Regex("const\\sval\\s([a-zA-Z]*)|val\\s([a-zA-Z]*)|lateinit\\svar\\s([a-zA-Z]*)|var\\s([a-zA-Z]*)|this")

		variables.findAll(html).forEach {
			html = html.replace(
				it.value,
				it.value
					.replace("var", "<span class=\"variable\">var</span>")
					.replace("val", "<span class=\"variable\">val</span>")
					.replace("const val", "<span class=\"variable\">const val</span>")
					.replace("lateinit var", "<span class=\"variable\">lateinit var</span>")
			)
		}

		val whenIfElseThrow = Regex("when\\s\\(.*\\)|if\\s\\(.*\\)|else\\s|throw\\s")

		whenIfElseThrow.findAll(html).forEach {
			html = html.replace(
				it.value,
				it.value
					.replace("when", "<span class=\"conditional\">when</span>")
					.replace("if", "<span class=\"conditional\">if</span>")
					.replace("else", "<span class=\"conditional\">else</span>")
					.replace("throw", "<span class=\"conditional\">throw</span>")
			)
		}

		val returnRegex = Regex("return@([a-zA-Z]*)|return\\s")

		returnRegex.findAll(html).forEach { r ->
			val label = r.groupValues.getOrNull(1)
			println(r.groupValues)
			html = html.replace(
				r.value,
				"<span class=\"return\">return${if (label != null) "<span class=\"label\">@$label</span>" else ""}</span>"
			)
		}

		val function = Regex("(?:infix\\s|inline\\s|)(?!\")fun(?:\\s<reified\\sT>|\\s<T>|)\\s([a-zA-Z.]*)\\(")

		function.findAll(html).forEach {
			val functionName = it.groupValues.last()
			html = html.replace(
				it.value,
				it.value
					.replace("infix fun", "<span class=\"fnc\">infix fun</span>")
					.replace("inline fun", "<span class=\"fnc\">inline fun</span>")
					.replace("fun", "<span class=\"fnc\">fun</span>")
					.replace(functionName, "<span class=\"fnc-name\">$functionName</span>")
			)
		}

		val italicizedMethods = Regex("((mutableList|list|mutableSet|set)Of)|\\.(forEach)|\\.(filter)|\\.(apply)|\\.(let)|\\.(also)|\\.(run)|\\.(runCatching)|\\.(takeIf)|\\.(takeUnless)|\\.(first)|\\.(second)|\\.(last)|\\.(toList)|\\.(toSet)|\\.(toMutableList)|\\.(toMutableSet)")

		italicizedMethods.findAll(html).forEach {
			val method = it.groupValues.first()
			println(it.groupValues)
			html = html.replace(
				it.value,
				it.value
					.replace(method, "<span class=\"itc\">$method</span>")
			)
		}

		return html
	}

	fun test() {
		val example = "<h3>?. and ?:</h3><p>Most of the time, I can fold a lot of my null checking into an elvis operator (<code>?:</code>). For example:</p><pre><code class=\"language-kotlin\">UserService.getById(id) ?: throw Exception(\"User couldn't be found\")\n" +
				"</code></pre><p>But in Java, I'd have to do all this...</p><pre><code class=\"language-java\">User user = UserService.getById(id);\n" +
				"\n" +
				"if (Objects.isNull(user)) {\n" +
				"    throw new Exception(\"User couldn't be found\");\n" +
				"}\n" +
				"</code></pre>"

		highlight(example)
	}
}