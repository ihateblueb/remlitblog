## Introduction

I am kotlin girl. Listen to my words.

## Syntax

JavaScript. Womp womp.

## Null Safety

## Data Classes

Record classes are not the same

## Extension Functions

## Inline Functions

## Infix Functions

## Trying to be Functional

Kotlin tries to appeal to people who like functional programming. At least a little bit.

## Coroutines

## Serialization

## Multiplatform

I never really wanted to touch KMP, but in Aster I was able to move a bunch of my data classes for common objects compile
to a TypeScript library and be used in Kotlin. It's been nice to have types in the frontend exactly as they are in the
backend.

## Objects

One of my biggest annoyances with Kotlin is the lack of statics. Instead, it has objects and companion objects. Objects
work like a Java class with all statics would, and companion objects act like an embedded all static class. This works fine
in Kotlin which is designed for it to work like statics would, but in Java it's super ugly.

```kotlin
class Book {
    fun read() {}
	
	companion object {
        fun create() {}
	}
}
```

```java
Book.Companion.create();
```

This is addressed in Kotlin with `@JvmStatic`. With this annotation, static methods are generated alongside the companion
object. Still, it's just a patch on default annoying behaviour.

## Honorable Mentions

### Cool Annotations

There's a bunch of built-in annotations for the JVM target that sound cool, but I never end up using them. One example of
that is `@JvmSynthetic` which makes it so only Kotlin code can use the annotated method.

## Footnotes