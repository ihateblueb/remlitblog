## Introduction

My first exposure to Java was around 2019 when I was really into running Minecraft servers (I still am, I love a good 
Towny server)
. I wanted to make my own Spigot<sup>[1]</sup> plugin. I had no ideas, no experience, and my laptop barely 
ran Minecraft, so IntelliJ barely ran, too. I didn’t touch Java for another 2 years, when I wrote my actual first Spigot
plugin. It was inspired by a custom plugin on a server I once loved. It let every player set a join and leave message
that sent after the vanilla join/leave. I finished the plugin in (I think) under a week, and I was very proud of myself.
While 2026 me has a lot to say about that old code, it could have been worse. This era also introduced me into the concept
of shadowing dependencies.

## What is Shadowing?

Shadowing usually refers to creating JARs, java archives, which include dependencies inside. Shadowing can also be called
making a fat or uber jar. JARs are essentially zip files full of resources and directories and compiled class files. When
you shadow dependencies, each dependency's contents are put into your program's JAR. Sounds fine, no problems with that,
right?

## Oh No...

Aster is my main programming project, and it's written in Kotlin. It has 51 direct dependencies. When I unzip Aster's JAR...

![A zoomed out list view in Finder. a bunch of folders with names of programming languages, programs, and tlds are visible](../static/img/posts/java-archives-and-dependencies/Screenshot%202026-02-19%20at%2016.12.01%402x.png)
![Finder window with three files visible, ASL-2.0.txt, LGPL-3.0.txt, and LICENSE.txt](../static/img/posts/java-archives-and-dependencies/Screenshot%202026-02-19%20at%2016.15.40%402x.png)

Holy shit. Erlang? Go? C#? In my JAR file? What? Not only that, there's three license files in here (none of which are Aster's),
and PHP?? (shown below)

There's not actually any Erlang, Go, or C# source in here, it's mustache files documenting APIs and such. I'm unsure what
depends on these and adds them, but that's unimportant to the topic. The PHP was actual source though.

![Finder window searching in the unzipped JAR for php, there's 8 visible PHP files selected](../static/img/posts/java-archives-and-dependencies/Screenshot%202026-02-19%20at%2018.30.07%402x.png)

## More Examples

I've mentioned before I like running Minecraft servers, and I still do to this day. On the server I run, we run a lot of 
plugins, 57 in total. I wrote a program that unzipped them all, and tried seeing if there was any overlap in classes. I didn't
find any overlap since these plugins relocate (more on that later). There's a lot of libraries included though.

![Screenshot 2026-02-19 at 20.03.44@2x.png](../static/img/posts/java-archives-and-dependencies/libs/Screenshot%202026-02-19%20at%2020.03.44%402x.png)

GSON (which exists more than once in here) and Netty...

![Screenshot 2026-02-19 at 20.04.12@2x.png](../static/img/posts/java-archives-and-dependencies/libs/Screenshot%202026-02-19%20at%2020.04.12%402x.png)
![Screenshot 2026-02-19 at 20.04.16@2x.png](../static/img/posts/java-archives-and-dependencies/libs/Screenshot%202026-02-19%20at%2020.04.16%402x.png)

This one's especially crazy, there's relocated dependencies in the relocated dependency!

![Screenshot 2026-02-19 at 20.04.33@2x.png](../static/img/posts/java-archives-and-dependencies/libs/Screenshot%202026-02-19%20at%2020.04.33%402x.png)

This section was shortened so half this post isn't just screenshots of a terminal.

## Solutions, ish

I use Paper as an example a lot because they have a lot of interesting problems, and a lot of interesting solutions. Their
tools and APIs are awesome. [Paper's experimental dependency management](https://docs.papermc.io/paper/dev/getting-started/paper-plugins/#loaders)
is very cool. When Paper loads a plugin, it checks 
if it requires any dependencies, then resolves them to a shared libraries folder that can be used by every plugin on the 
server. When that plugin needs them, they're added to the classpath. I've been using it recently rather than shadowing 
dependencies that don't require being shadowed.

This has been similar something I've wanted to do for all my projects, where there's a shared folder on the system for 
dependencies to go, and they're used across them all. It's kind of a big project, I initially intended this to segue into 
"Hey look at my project that's like this!" but haven't finished (or even published a Git repo) yet.

Another potential solution is relocation, although that's a half solution (which, even then, seems too much. a 1/4th solution.). 
Relocation is when your shadowed dependency is 
put at a different path (e.g. `io.netty` -> `site.remlit.project.libs.io.netty`). The JAR itself is a little less messy, 
if loading multiple JARs at the same time there's less of a potential for clashing dependencies, but your JARs are still 
big. This also only moves that specified package, not any resources associated with it.

Alternatively, you could also say fuck it! Fuck the users, they can figure it out themselves! Have fun, losers! Good luck 
figuring out the version for all 51 of my dependencies! That probably wouldn't work out great for adoption of your project 
though.

Thanks for reading, hope you're having a good new year! ❤️

## Footnotes
**1:** Spigot at one point was the Paper of the time, performant and better than it's predecessor, Bukkit. If you have no
        idea what any of these are, Spigot is a Minecraft server software that supports vanilla clients and server side mods.