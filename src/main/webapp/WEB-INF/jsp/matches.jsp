<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="matches" value="${requestScope.matches}"/>
<c:set var="currentPage" value="${requestScope.currentPage}"/>
<c:set var="totalPages" value="${requestScope.totalPages}"/>

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
                <input type="text" name="filter_by_player_name" placeholder="Filter by name"
                       class="search-input" value="${param.filter_by_player_name}">
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
            <div>
                <c:if test="${currentPage > 1}">
                    <a href="<c:url value="/matches?page=${currentPage - 1}&filter_by_player_name=${param.filter_by_player_name}"/>">Prev</a>
                </c:if>

                <span>Page ${currentPage} of ${totalPages}</span>

                <c:if test="${currentPage < totalPages}">
                    <a href="<c:url value="/matches?page=${currentPage + 1}&filter_by_player_name=${param.filter_by_player_name}"/>">Next</a>
                </c:if>
            </div>
        </main>
        <script src="<c:url value="/js/searchFilter.js"/>"></script>
    </body>
</html>
