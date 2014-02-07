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

<div class="container">
    <h2>Index</h2>
    <a href="/<%= Constants.CONTEXT_NAME %>/token/list">Token Management</a> |
    <a href="/<%= Constants.CONTEXT_NAME %>/logout"> Logout</a>
    <hr/>

    <h6>Username : ${username}</h6>

</div>

<script href="/<%= Constants.CONTEXT_NAME %>/resources/bootstrap/js/bootstrap.js"></script>

</body>
</html>