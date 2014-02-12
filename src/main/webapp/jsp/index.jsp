<%@ page import="uk.co.b2esoftware.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Index</title>

    <link href="/<%= Constants.CONTEXT_NAME %>/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<nav role="navigation" class="navbar navbar-default">
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
        <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a href="#" class="navbar-brand">Token Auth POC</a>
    </div>
    <!-- Collection of nav links and other content for toggling -->
    <div id="navbarCollapse" class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
            <li class="active"><a href="/<%= Constants.CONTEXT_NAME %>/">Home</a></li>
            <li><a href="/<%= Constants.CONTEXT_NAME %>/token/list">Token Management</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="/<%= Constants.CONTEXT_NAME %>/logout"> Logout</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="#">Signed in as ${username}</a></li>
        </ul>
    </div>
</nav>

</body>
</html>