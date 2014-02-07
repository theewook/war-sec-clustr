<%@ page import="uk.co.b2esoftware.Constants" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Token Management</title>

    <link href="/<%= Constants.CONTEXT_NAME %>/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <style>
        .custab
        {
            border: 1px solid #ccc;
            padding: 5px;
            margin: 5% 0;
            box-shadow: 3px 3px 2px #ccc;
            transition: 0.5s;
        }
        .custab:hover
        {
            box-shadow: 3px 3px 0px transparent;
            transition: 0.5s;
        }
    </style>
</head>

<body>

<div class="container">
    <h2>Token Management</h2>
    <a href="/<%= Constants.CONTEXT_NAME %>/">Index</a> |
    <a href="/<%= Constants.CONTEXT_NAME %>/logout"> Logout</a>
    <hr/>

    <div class="row custyle">
        <table class="table table-bordered table-hover custab">
            <thead>
            <a href="/<%= Constants.CONTEXT_NAME %>/token/generate/ROLE_USER" class="btn btn-primary btn-xs" role="button"><span class="glyphicon glyphicon-plus"></span> Generate User token</a>&nbsp;&nbsp;
            <a href="/<%= Constants.CONTEXT_NAME %>/token/generate/ROLE_USER,ROLE_ADMIN" class="btn btn-success btn-xs" role="button"><span class="glyphicon glyphicon-plus"></span> Generate Admin token</a>
            <tr>
                <th>Token</th>
                <th>Role</th>
                <th>Expiry Date</th>
                <th class="text-center">Action</th>
            </tr>
            </thead>
            <c:forEach items="${tokens}" var="token">
                <tr>
                    <td><c:out value="${token.token}"/></td>
                    <td>
                        <c:forEach items="${token.roles}" var="role">
                            <c:out value="${role.authority}"/><br/>
                        </c:forEach>
                    </td>
                    <td><c:out value="${token.expiryDateFormatted}"/></td>
                    <td class="text-center"><a href="/<%= Constants.CONTEXT_NAME %>/token/delete/<c:out value="${token.token}"/>" class="btn btn-danger btn-xs"><span class="glyphicon glyphicon-remove"></span> Delete</a></td>
                </tr>
            </c:forEach>
        </table>
    </div>

</div>

<script href="/<%= Constants.CONTEXT_NAME %>/resources/bootstrap/js/bootstrap.js"></script>

</body>
</html>