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

 1. export JAVA_HOME=/path/to/java/8 # Make sure you’re using java 8
 2. ./gradlew assemble # Build
 3. ./gradlew check # Run test suites

[1]: https://gradle.org/
