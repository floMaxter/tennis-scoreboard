<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="matches" value="${requestScope.matches}"/>

<html>
    <head>
        <title>Completed matches</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>">
    </head>
    <body class="wrapper">
        <main class="matches-container">
            <h2 class="matches-title">Matches</h2>
            <form action="<c:url value="/matches"/>" method="get" class="search-form">
                <input type="text" name="filter_by_player_name" placeholder="Filter by name" class="search-input" value="${param.name}">
                <input type="submit" value="Find" class="action-matches-button">
                <a href="<c:url value="/matches"/>" class="action-matches-button">Reset</a>
            </form>
            <table class="matches-table">
                <thead>
                    <tr>
                        <th>First Player</th>
                        <th>Second Player</th>
                        <th>Winner</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="match" items="${matches}">
                        <tr>
                            <td>${match.firstPlayer.name}</td>
                            <td>${match.secondPlayer.name}</td>
                            <td>${match.winner.name}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </main>
    </body>
</html>
