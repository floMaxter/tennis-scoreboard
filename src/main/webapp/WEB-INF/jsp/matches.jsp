<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="matches" value="${requestScope.matches}"/>

<html>
    <head>
        <title>Completed matches</title>
    </head>
    <body>
        <h1>Completed matches</h1>
        <table>
            <tr>
                <th>First player</th>
                <th>Second player</th>
                <th>Winner</th>
            </tr>
            <c:forEach var="match" items="${matches}">
                <tr>
                    <td>${match.firstPlayer.name}</td>
                    <td>${match.secondPlayer.name}</td>
                    <td>${match.winner.name}</td>
                </tr>
            </c:forEach>
        </table>
    </body>
</html>
