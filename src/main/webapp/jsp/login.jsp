<%@ page import="uk.co.b2esoftware.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>User Sign In</title>

    <link href="/<%= Constants.CONTEXT_NAME %>/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="/<%= Constants.CONTEXT_NAME %>/resources/css/signin.css" rel="stylesheet">
</head>

<body>

<div class="container">

    <div class="row">
        <div class="span4 offset4 well">
            <legend>Please Sign In</legend>

            <c:choose>
                <c:when test="${error}">
                    <div class="bs-example">
                        <div class="alert alert-danger">
                            <strong>Error!</strong> Wrong username/password combination. Please try again.
                        </div>
                    </div>
                </c:when>
            </c:choose>

            <form class="form-signin" role="form" name='f' action='j_security_check' method="POST">
                <input type="text" id="j_username" name="j_username" class="form-control" placeholder="Username" required autofocus>
                <input type="password" id="j_password" name="j_password" class="form-control" placeholder="Password" required>
                <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
            </form>
        </div>
    </div>
</div>

<script href="/<%= Constants.CONTEXT_NAME %>/resources/bootstrap/js/bootstrap.js"></script>

</body>
</html>