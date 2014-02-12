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
    <c:choose>
        <c:when test="${error}">
            <div class="bs-example">
                <div class="alert alert-danger">
                    <strong>Error!</strong> Wrong username/password combination. Please try again.
                </div>
            </div>
        </c:when>
    </c:choose>
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Please sign in</h3>
                </div>
                <div class="panel-body">
                    <form accept-charset="UTF-8" role="form" name='f' action='j_security_check' method="POST">
                        <fieldset>
                            <div class="form-group">
                                <input class="form-control" type="text" name="j_username" class="form-control" placeholder="Username" required autofocus>
                            </div>
                            <div class="form-group">
                                <input class="form-control" type="password" name="j_password" class="form-control" placeholder="Password" required>
                            </div>
                            <input class="btn btn-lg btn-success btn-block" type="submit" value="Login">
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script href="/<%= Constants.CONTEXT_NAME %>/resources/bootstrap/js/bootstrap.js"></script>

</body>
</html>