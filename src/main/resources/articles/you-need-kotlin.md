## Introduction

I am kotlin girl. Listen to my words.

## Syntax

JavaScript. Womp womp.

## Null Safety

## Data Classes

Record classes are not the same

## Extension Functions

Extension functions are a big part of Kotlin, it's own HTTP framework is mostly based around them. Extension functions
are when you extend a class by specifying it in front of your function method.

```kotlin
fun Application.module() {}
```

This function extends the Application class, and is now available like a method and just as `module()` in the current file.

My biggest use case for this was when I moved all my common API responses to a shared project that could be used for multiple
platforms. I used to have the database entity -> API response convertor functions on the companion objects of the data classes.
So, when I moved them to the common project, the database entity classes were no longer available and I had to move the
convertors to an extension function, like this:

```kotlin
fun Invite.Companion.fromEntity(entity: InviteEntity): Invite = Invite(
	id = entity.id.toString(),
	code = entity.code,
	// ...
	createdAt = entity.createdAt,
	usedAt = entity.usedAt
}
```

// Research: Java Interop
This extends the companion object so it's available like a static. I'll have more to say about those later on.

## Inline Functions

Kotlin loves lambdas, and inline functions are entirely based around them. 

<reified T>

## Infix Functions

Infix functions are probably one of my favorite Kotlin features. They seem weird to begin with, but once you see how
they're used by others they feel very clever.

For example, Jetbrains Exposed (the Kotlin ORM) uses them to build query strings:

```kotlin
UserEntity
	.find {
		UserTable.host eq null and
		(UserTable.username neq "instance.actor") and
		(UserTable.createdAt lessThan TimeService.now())
	}
	.toList()
```

Another example is my math project I wrote for Set Theory:

```kotlin
println(a isDisjoint b)
println(a union (c complement u))
println(a intersection b intersection c)
println((h intersection k) diff j complement u)
println((a complement u) intersection (b complement u))
```

They let you write natural feeling code that just flows and is easily fit anywhere, and I love that.

## Trying to be Functional

Kotlin tries to appeal to people who like functional programming. At least a little bit.

## Coroutines

// Research

Only recently have I started really working with coroutines where I need to know everything about how they work. My first
impression wasn't positive though, I just thought "Oh, this is annoying." Now that I've actually had to use them, I
am alright with them. I like that they are more effecient with thread usage, which generally helps large scale servers
like Aster run butter than if they were in Java. 

Coroutines start off at a dispatcher, and then the `suspend` (async) task is sent off to an available worker.

## Serialization

Kotlin serialization has been wonderful for my use case. I only had one hiccup when it came to ActivityPub's `object` property
which can be either an object or the ID of an object. For the most part, it's write a data class, throw on `@Serializable`,
and you're good to go. It's fast too, even with my weird rules and custom serializer. 

## Multiplatform

I never really wanted to touch KMP, but in Aster I was able to move a bunch of my data classes for common objects compile
to a TypeScript library and be used in Kotlin. It's been nice to have types in the frontend exactly as they are in the
backend.

## Objects

One of my biggest annoyances with Kotlin is the lack of statics. Instead, it has objects and companion objects. Objects
work like a Java class with all statics would, and companion objects act like an embedded all static class.

So, these:

```kotlin
object Bookshelf {
	val books = mutableListOf<Book>()
	fun tipOver() {}
}
```

```kotlin
class Book {
    fun read() {}
	
	companion object {
        fun create() {}
	}
} 
```

...are equivalent to these:

```Java
class Bookshelf {
	public static MutableList<Book> books = mutableListOf<Book>()
	public static void tipOver() {}
}
```

```Java
class Book {
	public static void read() {}

	class Companion {
		public static void create() {}
	}
}
```

I think the generated Companion subclass is unbeleivably ugly and annoying.

This can be fixed with `@JvmStatic`. With this annotation, static methods are generated alongside the companion object.
Still, it's just a patch on default annoying behaviour. Having objects is not something I'm against, but if it's at the
cost of statics in classes, I'm not a big fan.

This is an issue that's been debated for years, has had multiple KEEP<sup>[1]</sup> discussions, and may or may not be
resolved eventually. For now, I'm fine with the language as is. This is my only major complaint, and it'd be less of a
thing for me if I was willing to make all my Service classes in Aster objects to begin with. It just feels weird to make
them objects. Overall, it is not a big deal.

## Honorable Mentions

### Cool Annotations

There's a bunch of built-in annotations for the JVM target that sound cool, but I never end up using them. One example of
that is `@JvmSynthetic` which makes it so only Kotlin code can use the annotated method.

## Footnotes

**1:** Kotlin Evolution and Enhancement Process, the Kotlin JEP equivalent
