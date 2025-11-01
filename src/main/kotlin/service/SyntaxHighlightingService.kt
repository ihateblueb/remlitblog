package site.remlit.service

object SyntaxHighlightingService {
	const val HTML_OPEN_C = "&lt;"
	const val HTML_CLOSED_C = "&gt;"

	const val QUOTE = "(&quot;|&#34;|\")"

	fun createCodeblockRegex(language: String) =
		Regex("<code class=\"language-$language\">([\\s\\S]*?)</code>")

	fun highlight(html: String): String {
		var html = html
		val kotlinBlocks = createCodeblockRegex("kotlin").findAll(html)
		val javaBlocks = createCodeblockRegex("java").findAll(html)

		kotlinBlocks.forEach {
			html = html.replace(it.value, highlightKotlin(it.value))
		}
		javaBlocks.forEach {
			html = html.replace(it.value, highlightJava(it.value))
		}

		return html
	}

	fun highlightJava(value: String): String {
		var html = value

		val strings = Regex("$QUOTE([\\s\\S]*?)$QUOTE")

		strings.findAll(html).forEach {
			if (!it.value.contains("\"language-java\""))
				html = html.replace(it.value, "<span class=\"string\">${it.value}</span>")
		}

		val comment = Regex("//(.*)")

		comment.findAll(html).forEach {
			html = html.replace(it.value, "<span class=\"comment\">${it.value}</span>")
		}

		val switchIfElseThrow = Regex("switch\\s\\(.*\\)|if\\s\\(.*\\)|else\\s|throw\\s")

		switchIfElseThrow.findAll(html).forEach {
			html = html.replace(
				it.value,
				it.value
					.replace("switch", "<span class=\"conditional\">switch</span>")
					.replace("if", "<span class=\"conditional\">if</span>")
					.replace("else", "<span class=\"conditional\">else</span>")
					.replace("throw", "<span class=\"conditional\">throw</span>")
			)
		}

		val method = Regex("(public|private)\\s(static\\s|)(void|[A-Za-z$HTML_OPEN_C$HTML_CLOSED_C.]*)\\s([a-zA-Z]*)\\(")

		method.findAll(html).forEach {
			val name = it.groupValues.last()
			html = html.replace(
				it.value,
				it.value
					.replace("public", "<span class=\"mtd\">public</span>")
					.replace("private", "<span class=\"mtd\">private</span>")
					.replace("static", "<span class=\"mtd\">static</span>")
					.replace("void", "<span class=\"mtd\">void</span>")
					.replace(name, "<span class=\"mtd-name\">$name</span>")
			)
		}

		val properties = Regex("(public|private)\\s(static\\s|final\\s|)(void|[A-Za-z$HTML_OPEN_C$HTML_CLOSED_C.]*)\\s([a-zA-Z]*)\\s=")

		properties.findAll(html).forEach {
			val name = it.groupValues.last()
			html = html.replace(
				it.value,
				it.value
					.replace("public", "<span class=\"variable\">public</span>")
					.replace("private", "<span class=\"variable\">private</span>")
					.replace("static", "<span class=\"variable\">static</span>")
					.replace("final", "<span class=\"variable\">final</span>")
					.replace("void", "<span class=\"variable\">void</span>")
					.replace(name, "<span class=\"variable-name\">$name</span>")
			)
		}

		val classInterface = Regex("class\\s([a-zA-Z]*)".let {
			var regex = it
			regex += "|record\\sclass\\s([a-zA-Z]*)"
			regex += "|abstract\\sclass\\s([a-zA-Z]*)"
			regex += "|sealed\\sclass\\s([a-zA-Z]*)"
			regex += "|enum\\sclass\\s([a-zA-Z]*)"
			regex += "|static\\sclass\\s([a-zA-Z]*)"

			regex += "|interface\\s([a-zA-Z]*)"

			regex
		})

		classInterface.findAll(html).forEach {
			html = html.replaceFirst(
				it.value,
				it.value
					.replace("record class", "<span class=\"clazz\">record class</span>")
					.replace("abstract class", "<span class=\"clazz\">abstract class</span>")
					.replace("sealed class", "<span class=\"clazz\">sealed class</span>")
					.replace("enum class", "<span class=\"clazz\">enum class</span>")
					.replace("static class", "<span class=\"clazz\">static class</span>")
					 //.replace("class", "<span class=\"clazz\">class</span>")
					.replace("interface", "<span class=\"clazz\">interface</span>")
			)
		}

		return html
	}

	fun highlightKotlin(value: String): String {
		var html = value

		val strings = Regex("$QUOTE([\\s\\S]*?)$QUOTE")

		strings.findAll(html).forEach {
			if (!it.value.contains("\"language-kotlin\""))
				html = html.replace(it.value, "<span class=\"string\">${it.value}</span>")
		}

		val comment = Regex("//(.*)")

		comment.findAll(html).forEach {
			html = html.replace(it.value, "<span class=\"comment\">${it.value}</span>")
		}

		val booleanNull = Regex("true|false|null")

		booleanNull.findAll(html).forEach {
			html = html.replace(
				it.value,
				it.value
					.replace("true", "<span class=\"true\">true</span>")
					.replace("false", "<span class=\"false\">false</span>")
					.replace("null", "<span class=\"null\">null</span>")
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
					.replace("data class", "<span class=\"clazz\">data class</span>")
					.replace("open class", "<span class=\"clazz\">open class</span>")
					.replace("abstract class", "<span class=\"clazz\">abstract class</span>")
					.replace("sealed class", "<span class=\"clazz\">sealed class</span>")
					.replace("enum class", "<span class=\"clazz\">enum class</span>")
					.replace("value class", "<span class=\"clazz\">value class</span>")
					.replace("class", "<span class=\"clazz\">class</span>")
					.replace("companion object", "<span class=\"clazz\">companion object</span>")
					.replace("object", "<span class=\"clazz\">object</span>")
					.replace("interface", "<span class=\"clazz\">interface</span>")
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
			html = html.replace(
				r.value,
				"<span class=\"return\">return${if (label != null) "<span class=\"label\">@$label</span>" else ""}</span>"
			)
		}

		val function = Regex("(?:infix\\s|inline\\s|)(?!\")fun(?:\\s${HTML_OPEN_C}reified\\sT${HTML_CLOSED_C}|\\s${HTML_OPEN_C}T${HTML_CLOSED_C}|)\\s([a-zA-Z.]*)\\(")

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
			html = html.replace(
				it.value,
				it.value
					.replace(method, "<span class=\"itc\">$method</span>")
			)
		}

		val t = Regex("${HTML_OPEN_C}T${HTML_CLOSED_C}|\\sT${HTML_CLOSED_C}|\\(T\\)")

		t.findAll(html).forEach {
			html = html.replace(it.value, it.value.replace("T", "<span class=\"t\">T</span>"))
		}

		val thisRegex = Regex("(\\s|\\()this(\\.|\\s)")

		thisRegex.findAll(html).forEach {
			html = html.replace(it.value, it.value.replace("this", "<span class=\"this\">this</span>"))
		}

		val stringTemplating = Regex("\\$(\\{([a-zA-Z0-9.()]*)}|([a-zA-Z0-9]*))")

		stringTemplating.findAll(html).forEach {
			println(it.groupValues)
			html = html.replaceFirst(
				it.value,
				"<span class=\"strtmp\">${it.value}</span>"
			)
		}

		return html
	}
}