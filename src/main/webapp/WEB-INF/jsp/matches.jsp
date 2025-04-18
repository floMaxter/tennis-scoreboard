<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="filterByPlayerName" value="${param.filter_by_player_name}"/>
<c:set var="currentPage" value="${requestScope.matchPage.currentPage}" />
<c:set var="totalPages" value="${requestScope.matchPage.totalPages}" />
<c:set var="matches" value="${requestScope.matchPage.matches}" />

<html>
    <head>
        <title>Completed matches</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>">
        <link rel="icon" type="image/png" href="<c:url value='/images/favicon.ico'/>">
    </head>
    <body class="wrapper">
        <%@ include file="header.jsp"%>
        <main class="matches-container">
            <h2 class="matches-title">Matches</h2>
            <form action="<c:url value="/matches"/>" method="get" class="search-form">
                <input type="text" name="filter_by_player_name" placeholder="Filter by name"
                       class="search-input" value="${filterByPlayerName}">
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

                    <c:if test="${empty matches}">
                        <tr>
                            <td colspan="3" class="no-matches-message">
                                No matches found
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
            <div class="pagination-container">
                <c:url var="prevUrl" value="/matches">
                    <c:param name="page" value="${currentPage - 1}"/>
                    <c:if test="${not empty filterByPlayerName}">
                        <c:param name="filter_by_player_name" value="${filterByPlayerName}"/>
                    </c:if>
                </c:url>
                <a href="<c:url value="${currentPage > 1 ? prevUrl : '#'}"/>"
                   class="pagination-link ${currentPage > 1 ? '' : 'hidden'}">Prev</a>

                <span class="pagination-info">Page ${currentPage} of ${totalPages}</span>

                <c:url var="nextUrl" value="/matches">
                    <c:param name="page" value="${currentPage + 1}"/>
                    <c:if test="${not empty filterByPlayerName}">
                        <c:param name="filter_by_player_name" value="${filterByPlayerName}"/>
                    </c:if>
                </c:url>
                <a href="<c:url value="${currentPage < totalPages ? nextUrl : '#'}"/>"
                   class="pagination-link ${currentPage < totalPages ? '' : 'hidden'}">Next</a>
            </div>
        </main>
        <%@ include file="footer.jsp"%>
        <script src="<c:url value="/js/searchFilter.js"/>"></script>
        <script>
            window.actualPageFromServer = ${currentPage};
        </script>
        <script src="<c:url value="/js/normalizePageParam.js"/>"></script>
    </body>
</html>
