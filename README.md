spring-sec
==========

This is a sample WAR which uses Spring Security for role-based access control, but uses the JEE container to perform
J2EE "FORM-based" login authentication. Once the user has been authenticated, a token is generated and it will be used
throughout the session.

Installation
------------

###Prerequisites###

* A MySQL database is needed
* Create a new schema called `springsec`
* Run the `setup.sql` from the `sql` folder

###Default Configuration###

By default, the application uses this configuration

    # Database configuration
    database.driver=com.mysql.jdbc.Driver
    database.url=jdbc:mysql://localhost:3306/springsec
    database.user=root
    database.password=
    hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
    hibernate.show_sql=true

    # Scheduler configuration
    cleanSession.fixedDelay=10000

These values can be changed in the file `app.properties`.

The default timeout for the session is 1 minute.

Running the application
-----------------------

###Running in Jetty###

    mvn jetty:run

Jetty is configured through the maven plugin in the `pom.xml` file and defines a security realm using the file
`realm.properties`.

###Running in WebLogic 12###

The build includes a shim descriptor `weblogic.xml` for deployment into WebLogic.  The principal names `ROLE_USER`
and `ROLE_ADMIN` in `weblogic.xml` need to be defined in the WebLogic security realm and associated with appropriate
users.

**Caution:** the application doesn't work out-of-the box on Weblogic 11g because of the use of JPA 2. Weblogic 11g
is JAVA EE 5 certified. However JPA 2 is not part of the JAVA EE 5. So since WebLogic Server implements the Java EE 5
specification, it is not required to support JPA 2. Workaround are possible but they haven't been tested.

###Running in GlassFish###

Similar to WebLogic, but the shim file is glassfish-web.xml

Rationale
---------

###Authentication via the GUI###

The purpose of this sample is to show the use of Spring Security in a JEE web app that uses the servlet container to
perform the authentication. Spring Security refers to this scenario as *pre-authentication* and includes some stuff
to support it. Once the user has been successfully authenticated, a token is generated and it will be used to perform
the following requests. The servlet container is only used to do the first authentication and to retrieve the
roles associated with the user.

Although SpringSecurity can just as easily be used to perform authentication itself in a number of ways, many
customers have operational departments that deploy and monitor supplier software in application server consoles like
those of WebLogic and WebSphere (no, really!).  And their expectation is that they should be able to set up users and
groups/roles in the app server in the same way that they have for all their other apps - the app server's
*security realm* is likely hooked into a shared instance of an LDAP server or Active Directory, or whatever, that
is shared across the organisation.

It's been around for ages; J2EE FORM login (or should we by now really be saying JEE?).  It allows the container to
intercept and authenticate the user's credentials, using the app-supplied login form.  This is achieved with the
following familiar blocks in the `web.xml` file.

    <login-config>
        <auth-method>FORM</auth-method>
        <form-login-config>
            <form-login-page>/login</form-login-page>
            <form-error-page>/loginfailed</form-error-page>
        </form-login-config>
    </login-config>

This one tells the container that the `FORM` scheme is being used and into which URL the user's will be sticking their
username and password.  The supplied login form HTML just has to follow the following contract to work.

    <form method=post action="j_security_check">
        <input type="text" name= "j_username">
        <input type="password" name= "j_password">
    </form>

The POST request can then be intercepted by the container, who validates the credentials against their preferred
authentication engine.  In addition, as app suppliers, we often define some security roles in the `web.xml` whose
names are meaningful to the app.

    <security-role>
        <role-name>user</role-name>
    </security-role>
    <security-role>
        <role-name>operator</role-name>
    </security-role>
    <security-role>
        <role-name>administrator</role-name>
    </security-role>

Our intention is that when some user logs in, we want to know what roles they have.  E.g., they have to be in
the `user` role to see anything at all; if they are in the `operator` role they can see *these* pages, and if they are
in the `administrator` role they can also see *those* pages and so on and so forth.

The container's security realm associates users with roles (or groups), but the names may be different.  E.g., a
customer may have a WebLogic installation with an LDAP-backed security realm.  This realm may already have users with
'roles' such as `normal_users`, `operator_users` and `admin_users` to their names.  Without changing the setup of
their LDAP server, they would like the two concepts mapped.  This is often done using a *shim* descriptor file such
as `weblogic.xml` for WebLogic.

    [weblogic.xml]
    ...
    <security-role-assignment>
        <role-name>user</role-name>                    << name in web.xml
        <principal-name>normal_users</principal-name>  << name in container's security realm
    </security-role-assignment>
    <security-role-assignment>
        <role-name>operator</role-name>
        <principal-name>operator_users</principal-name>
    </security-role-assignment>
    <security-role-assignment>
        <role-name>admin</role-name>
        <principal-name>admin_users</principal-name>
    </security-role-assignment>

Although having such a file distributed with your app is not exactly the most flexible solution, since the customer is
unlikely to want to unzip your WAR and edit it, it illustrates the point.  In reality, app servers like WebLogic also
allow the customer to specify some form of *deployment plan* during installation that can override such default mappings.

###Authentication via REST requests###

Regarding REST requests authentication, the servlet container is not needed. Each REST request will supply just a token
in its header. The token has to be manually generated via the Token Management UI and is associated with a set of
roles.


Versions
--------

* Spring `3.2.4.RELEASE`
* Spring Security `3.1.4.RELEASE`
* Hibernate `4.2.8.Final`
* Bootstrap `3.1.0`

Spring Logging
--------------

Spring's default is to use commons-logging which is a bit nasty.  The workaround, documented on Spring's own site, is to do the following.

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>3.2.4.RELEASE</version>
            <exclusions>
                <!-- We don't want commons-logging thanks -->
                <exclusion>
                    <groupId>commons-logging</groupId>
                    <artifactId>commons-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- This is what we have to do instead of commons-logging for Spring logging to work -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>jcl-over-slf4j</artifactId>
            <version>1.7.0</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.0</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>1.7.0</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.14</version>
            <scope>runtime</scope>
        </dependency>

Testing on the command line using cURL
--------------------------------------

###Authentication via the GUI###

This simple commandline sequence follows the FORM login without having to resort to the browser.

Make a request to the protected resource.

    $ curl -c cookies.txt -vL http://localhost:8080/spring-sec/

* -c stores the cookie information into the file 'cookies.txt' for later use
* -v verbose output (so you can see the redirect)
* -L make sure curl follows any redirects that the server may send

Assuming that you're not already logged in, this will land you on the login page.  You should see the HTML of the
login form coming back in the `curl` command line verbose output.  The token ID cookie now will have been written
to the file cookies.txt.

To now submit the login form with username and password (and token ID cookie):

    $ curl -vL "http://localhost:8080/spring-sec/j_security_check" \
        -d "j_username=testuser&j_password=passw0rd" -b cookies.txt -c cookies.txt

* -c file to write cookies to (cookie jar)
* -b file to send cookies from (same as above)
* -d tells curl to send a POST request with the specified name/value pairs (i.e. username,password)

You should now see a redirect taking you to the originally requested page (i.e., the welcome page; index.jsp).  Exactly
what redirects you get, and when, depends a bit on the app server.

The -c option is important this time around because the server *may* (or may not) want to replace the original cookie
with a new value following the login, so it has to have the cookie file to write to.  Whether or not the cookie is
replaced depends on the app server and how it's configured.

Now that you have a token ID cookie in the file cookies.txt, you can continue to make requests, re-sending the
token ID cookie until the server times out the token.  For example:

    $ curl -v http://localhost:8080/spring-sec/token/list -b cookies.txt

Now, we don't see any redirects, because we have supplied a valid token ID cookie directly in the request.  The output
from curl's verbose mode (-v) will look something like this

    > GET /spring-sec/token/list HTTP/1.1
    > User-Agent: curl/7.30.0
    > Host: localhost:8080
    > Accept: */*
    > Cookie: X-Token=5ugeug83s00p4sb1c7fl

Finally, to log yourself out, you need to send a request to the /logout URL. This servlet tells the container to
invalidate the token.

   $ curl -vL http://localhost:8080/spring-sec/logout -b cookies.txt

The servlet's response is a redirect to the context path, but because we're logged out, we should ultimately be
redirected back to the login page by whatever route the server sees fit.  The cookie in cookies.txt is now invalid and
no use to us.

###Authentication via REST requests###

This sample contains 2 REST services:

* `userservice` accessible with a ROLE_USER role
* `adminservice` accessible with a ROLE_ADMIN role

The first thing we have to do is to generate 2 tokens. One with a ROLE_USER role and another one with a ROLE_USER and
a ROLE_ADMIN role.

To do so, just log yourself to the application with your favorite browser. Click to Token Management in the menu. You
should be able to see 2 buttons called `Generate User token` and `Generate Admin token`. Click once on each of these
2 buttons. You can see now that 2 tokens have been generated and are ready to be used.

In my case `pml7d093ut7taciqbo8i` is my user token and `n2u4k6ec3hd7p2eadjea` is my admin token.

To now call the REST userservice with the generated user token:

    $ curl -vL http://localhost:8080/spring-sec/rest/userservice -H "X-Token: pml7d093ut7taciqbo8i"

The output from curl's verbose mode (-v) will look something like this

    > GET /spring-sec/rest/userservice HTTP/1.1
    > User-Agent: curl/7.30.0
    > Host: localhost:8080
    > Accept: */*
    > X-Token: pml7d093ut7taciqbo8i

You should also see the REST userservice response: `userservice - OK`

You can now do the same with the REST adminservice. Make sure to use the generated admin token. If you use a generated
user token and you try to call the REST adminservice you will get an access denied. Let's try that.

    $ curl -vL http://localhost:8080/spring-sec/rest/adminservice -H "X-Token: pml7d093ut7taciqbo8i"

The output from curl's verbose mode (-v) will look something like this

    > GET /spring-sec/rest/adminservice HTTP/1.1
    > User-Agent: curl/7.30.0
    > Host: localhost:8080
    > Accept: */*
    > X-Token: pml7d093ut7taciqbo8i
    >
    < HTTP/1.1 403 Access is denied

As you can see, we got an HTTP error 403 Access is denied.
