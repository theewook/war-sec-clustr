war-sec
=======

This is a sample WAR which uses Spring Security for role-based access control, but uses the JEE container to perform
J2EE "FORM-based" login authentication.

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

###Citation###

In putting this sample together, I referred to the Spring and Spring Security docs, and also
[this](https://github.com/skrall/spring-security-j2ee-preauth-example) handy example I found from
Steve Krall on github.

Rationale
---------

The purpose of this sample is to show the use of Spring Security in a JEE web app that uses the servlet container to
perform authentication.  Spring Security refers to this scenario as *pre-authentication* and includes some stuff to
support it.

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
            <form-login-page>/login.jsp</form-login-page>
            <form-error-page>/login_error.jsp</form-error-page>
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





To a certain extent you can specify such access control using the `security-constraint` element in the `web.xml` but
it gets increasingly cumbersome, and limiting, the more detailed your requirements are.

Spring Security has a sophisticated approach to access control, based on the authorities of the User Principal.  It is
this that we want to make use of, whilst honouring the authentication provided by the container.  To this end, it is
sufficient to simply configure the `security-constraint` in the web.xml to say that the user has to be at least in the
role of `user`, say, to access anything.

    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Everything</web-resource-name>
            <url-pattern>/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
    </security-constraint>

Then we let Spring Security take over to do the fine-tuned role-based access control.

Versions
--------

* Spring `3.2.4.RELEASE`
* Spring Security `3.1.4.RELEASE`

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

Jetty Plugin
------------

For simple development testing with `mvn jetty:run`

```xml
    <plugins>
        <!-- mvn jetty:run http://localhost:8080/spring-sec -->
        <plugin>
            <groupId>org.mortbay.jetty</groupId>
            <artifactId>maven-jetty-plugin</artifactId>
            <version>6.1.26</version>
        </plugin>
    </plugins>
```

WebLogic deployment
-------------------

For convenience of deployment to WebLogic, and to be on the safe side with class-loding policy.

		<!-- WebLogic shim.  Tested with WebLogic 11g (v 10.3.6) -->
		<weblogic-web-app xmlns="http://xmlns.oracle.com/weblogic/weblogic-web-app"
		    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		    xsi:schemaLocation="http://xmlns.oracle.com/weblogic/weblogic-web-app
		    http://xmlns.oracle.com/weblogic/weblogic-web-app/1.3/weblogic-web-app.xsd"
		    >
		    <description>Spring Security Sample</description>
		    <weblogic-version>10.3.6</weblogic-version>
		    <!-- Force policy of WAR class-loader first -->
		    <container-descriptor>
		        <prefer-web-inf-classes>true</prefer-web-inf-classes>
		    </container-descriptor>
		    <!-- Make WebLogic use a different session ID cookie name and path for this web application -->
		    <!-- Note, if this isn't done, bear in mind that the WebLogic console AND your app both use JSESSIONID by default -->
		    <!-- This causes confusion if debugging your app in a tab of the same browser as the WebLogic console -->
		    <!-- You apparently will be unable to log into your app because the cookie from the console is sent as well -->
		    <session-descriptor>
		        <cookie-name>spring-sec-session</cookie-name>
		        <cookie-path>spring-sec</cookie-path>
		    </session-descriptor>
		</weblogic-web-app>
		
Web App Descriptor
------------------

The web.xml provides the hooks necessary for Spring context loading, with a default config file of `WEB-INF/application.xml`.  It also sets up the 
Spring Security filter stack using the magic bean/filter name of `springSecurityFilterChain` which is defined by Spring Security at startup.

		<web-app>

		    <display-name>Spring Security Sample</display-name>

		    <!--
		       Spring security filter config
		         N.b. 'springSecurityFilterChain' is a magic bean name generated by Spring Security
		     -->
		    <filter>
		        <filter-name>springSecurityFilterChain</filter-name>
		        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
		    </filter>
		    <filter-mapping>
		        <filter-name>springSecurityFilterChain</filter-name>
		        <url-pattern>/*</url-pattern>
		    </filter-mapping>

		    <!-- Loads default Spring config file from WEB-INF/applicationContext.xml -->
		    <listener>
		        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
		    </listener>

		</web-app>
		
The Spring Context
------------------

The Spring Context, in `applicationContext.xml` defines the Spring Security beans using the Spring Security namespace.  It just uses a simple in-memory `userDetailsService` for password authentication.  It uses the `auto-config=true` setting that places a Form Login security filter in the stack.  

		<beans:beans xmlns="http://www.springframework.org/schema/security"
		    xmlns:beans="http://www.springframework.org/schema/beans"
		    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		    xsi:schemaLocation="http://www.springframework.org/schema/beans
		           http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
		           http://www.springframework.org/schema/security
		           http://www.springframework.org/schema/security/spring-security-3.1.xsd">

		    <http auto-config='true'>
		        <intercept-url pattern="/**" access="ROLE_USER" />
		    </http>

		    <user-service id="userDetailsService">
		        <user name="admin" password="password" authorities="ROLE_USER, ROLE_ADMIN" />
		        <user name="user" password="password" authorities="ROLE_USER" />
		    </user-service>

		    <authentication-manager alias="authenticationManager">
		        <authentication-provider user-service-ref="userDetailsService"/>
		    </authentication-manager>

		</beans:beans>

The client is redirected to the auto-generated login form when authentication is first declined.

Testing on the command line using cURL
--------------------------------------

This simple commandline sequence follows the FORM login without having to resort to the browser.

Make a request to the protected resource.

    $ curl -c cookies.txt -vL http://localhost:8080/spring-sec/

* -c stores the cookie information into the file 'cookies.txt' for later use
* -v verbose output (so you can see the redirect)
* -L make sure curl follows any redirects that the server may send

Assuming that you're not already logged in, this will land you on the login page.  You should see the HTML of the
login form coming back in the `curl` command line verbose output.  The session ID cookie now will have been written
to the file cookies.txt.

To now submit the login form with username and password (and session ID cookie):

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

Now that you have a session ID cookie in the file cookies.txt, you can continue to make requests, re-sending the
session ID cookie until the server times out the session.  For example:

    $ curl -v http://localhost:8080/spring-sec/index.jsp -b cookies.txt

Now, we don't see any redirects, because we have supplied a valid session ID cookie directly in the request.  The output
from curl's verbose mode (-v) will look something like this


    > GET /spring-sec/index.jsp HTTP/1.1
    > User-Agent: curl/7.21.4 (universal-apple-darwin11.0) libcurl/7.21.4 OpenSSL/0.9.8x zlib/1.2.5
    > Host: localhost:8080
    > Accept: */*
    > Cookie: JSESSIONID=kvd6n7k0o7hn1vz12ixzg0ja1
    >

Finally, to log yourself out, you need to send a request to the /logout URL (which we mapped to the LogoutServlet in
web.xml).  This servlet tells the container to invalidate the session.

   $ curl -vL http://localhost:8080/spring-sec/logout -b cookies.txt -X POST

Note the URL pattern /logout which invokes the LogoutServlet.  This servlet invalidates the session, causing the logout.
The servlet's response is a redirect to the context path, but because we're logged out, we should ultimately be
redirected back to the login page by whatever route the server sees fit.  The cookie in cookies.txt is now invalid and
no use to us.












