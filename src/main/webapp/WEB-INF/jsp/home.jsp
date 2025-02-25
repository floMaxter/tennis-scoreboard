<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Home page</title>
    </head>
    <body>
        <h1>Tennis score board</h1>
        <a href="${pageContext.request.contextPath}/new_match">New</a> <span> - start new match</span>
        <br>
        <a href="${pageContext.request.contextPath}/matches">Matches</a> <span> - list of finished matches</span>
    </body>
</html>
