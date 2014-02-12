spring-sec
==========

This is a sample WAR which uses Spring Security for role-based access control, but uses the JEE container to perform
J2EE "FORM-based" login authentication. Once the user has been authenticated, a token is generated and it will be used
throughout the session.

Installation
------------

###Prerequisites###

* A MySQL database is needed
* Create a new schema called `spring-sec`
* Run the `setup.sql` from the `sql` folder

###Default Configuration###

By default, the application uses this configuration

    # Database configuration
    database.driver=com.mysql.jdbc.Driver
    database.url=jdbc:mysql://localhost:3306/spring-sec
    database.user=root
    database.password=
    hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
    hibernate.show_sql=true

    # Scheduler configuration
    cleanSession.fixedDelay=10000

These values can be changed in the file `app.properties`

Running the application
-----------------------

###Running in Jetty###

    mvn jetty:run

Jetty is configured through the maven plugin in the `pom.xml` file and defines a security realm using the file
`realm.properties`.

###Running in WebLogic###

The build includes a shim descriptor `weblogic.xml` for deployment into WebLogic.  The principal names `ROLE_USER`
and `ROLE_ADMIN` in `weblogic.xml` need to be defined in the WebLogic security realm and associated with appropriate
users.

###Running in GlassFish###

Similar to WebLogic, but the shim file is glassfish-web.xml


Versions
--------

* Spring `3.2.4.RELEASE`
* Spring Security `3.1.4.RELEASE`
* Hibernate `4.2.8.Final`
* Bootstrap `3.1.0`








