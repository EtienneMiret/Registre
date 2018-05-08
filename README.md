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

    $ export JAVA_HOME=/path/to/java/8   # Make sure you’re using java 8
    $ ./gradlew assemble                 # Build
    $ ./gradlew check                    # Run test suites

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

You need to setup an SQL database. Any [database supported by EclipseLink][4]
will do, but you may need to translate the schema. Available schemas are in the
Model/src/main/sql directory. In any case, you need to run the schema yourself.

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
[4]: https://wiki.eclipse.org/EclipseLink/FAQ/JPA#What_databases_are_supported.3F

## Transfer data from Registre v2

In order to migrate data from a Registre v2 instance, you need to run
DataTransfer-3.x-all.jar. This jar can be found in the DataTransfer/build/libs
directory after the build phase. It requires the following on the classpath
to run:
 * any version of slf4j-api-1.x.y.jar,
 * the SLF4J implementation of your choice,
 * the JDBC driver for the Registre v2 database,
 * the JDBC driver for the Registre v3 database
 * and a `config.properties` file with the following properties set:
    * `oldDb.driver` (driver class of the Registre v2 database)
    * `oldDb.url` (JDBC URL of the Registre v2 database)
    * `oldDb.user` (user to use to connect to the v2 database)
    * `oldDb.pwd` (password to use to connect to the v2 database)
    * `newDb.driver` (driver class of the Registre v3 database)
    * `newDb.url` (JDBC URL of the v3 database)
    * `newDb.user` (user to use to connect to the v3 database)
    * `newDb.pwd` (password to use to connect to the v3 database)
    * `batchSize` (number of records to migrate in one transaction).

Note that the `-cp` option is ignored by java when the `-jar` option is issued.
So you probably want to put all those jars in a “libs” directory, the config
file in a “config” directory, and run:

    $ java -cp "$(printf '%s:' libs/*.jar)config" fr.elimerl.registre.transfer.DataTransfer

Also note that the target database needs to already have a schema.

## Configuring users

Only users who have their email registered in the database can connect to
Registre. Their is no way for them to register themselves. An administrator
must add them with the following SQL query:

    INSERT INTO users (name, email) VALUES ('<name>', '<email>');

If you migrated a Registre v2 database, you may already have a number of users,
but they will be laking an email address. So they need to be updated:

    UPDATE users SET email = '<email>' WHERE name = '<name>';
