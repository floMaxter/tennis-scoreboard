<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Home page</title>
    </head>
    <body>
        <h1>Tennis score board</h1>
        <a href="<c:url value="/new-match"/>">New</a> <span> - start new match</span>
        <br>
        <a href="<c:url value="/matches"/>">Matches</a> <span> - list of finished matches</span>
    </body>
</html>
