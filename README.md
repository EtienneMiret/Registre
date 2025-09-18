# Registre

## Introduction

“Registre” is a webapp intended to be used by a family or a group of friends
in order to allow each member to know which movies, books and comics other
members own. Members will register their own titles and be able to search all
registered entries. Thus helping not gifting another member a title they
already have.

## Build and test

Registre uses [Gradle][1] as its build tool. A great thing about Gradle is that
you don’t need to install it, you just need to run the “gradlew” wrapper
provided with the sources.

    $ ./gradlew assemble                 # Build
    $ ./gradlew check                    # Run test suites

[1]: https://gradle.org/

## Releases

This version is an attempt to rewrite this project in Kotlin, both for the
backend and the frontend.

Versions [3.x] were written in Java.

Versions [2.x] and [1.x] were written in PHP.

[1.x]: https://github.com/EtienneMiret/Registre/tree/1.x
[2.x]: https://github.com/EtienneMiret/Registre/tree/2.x
[3.x]: https://github.com/EtienneMiret/Registre/tree/3.x
