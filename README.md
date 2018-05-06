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

## Install and run

After the build phase, you should have a Server-3.X.war artifact in the
Server/build/libs directory. This is the main artifact you will need to deploy
to your Java web application server (in /usr/local/tomcat/webapps for a
standard Tomcat installation).

You will also need the following JARs in the classpath
(/usr/local/tomcat/lib for Tomcat):
 * slf4j-api-1.x.y.jar (any version will do, see [SLF4J compatibility][2])
 * the SLF4J implementation of your choice (it may be several JARs)
 * the JDBC driver of your choice

Finally, you need to configure the following JNDI resources (this is done in
/usr/local/tomcat/conf/context.xml for Tomcat):
 * `jdbc/Registre`: a container authenticated javax.sql.DataSource.
 * `clientId` and `clientSecret`: ID and secret of your Registre installation
   created with the [Google Console][3] for SSO support
   (both of java.lang.String type).
 * `redirectUri`: the full URL to the /login page of your Registre installation
   (of type java.lang.String).

[2]: https://www.slf4j.org/faq.html#compatibility
[3]: https://console.developers.google.com/
