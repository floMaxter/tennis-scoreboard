<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>New match</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
        <link rel="icon" type="image/png" href="<c:url value='/images/favicon.ico'/>">
    </head>
    <body class="wrapper">
        <%@ include file="header.jsp"%>
        <main class="center-container">
            <h2 class="new-match-title">New match</h2>
            <form action="<c:url value="/new-match"/>" name="players" method="post" autocomplete="on"
                  class="create-match-form">
                <label for="firstPlayerName" class="input-label">The name of the first player: </label>
                <input id="firstPlayerName" type="text" name="firstPlayerName" placeholder="Name" required class="create-input">
                <br>
                <label for="secondPlayerName" class="input-label">The name of the second player: </label>
                <input id="secondPlayerName" type="text" name="secondPlayerName" placeholder="Name" required class="create-input">
                <br>
                <input type="submit" value="Start" class="primary-button">
            </form>
            <c:if test="${not empty requestScope.errors}">
                <div>
                    <c:forEach var="error" items="${requestScope.errors}">
                        <span class="errors">${error.message}</span><br/>
                    </c:forEach>
                </div>
            </c:if>
        </main>
        <%@ include file="footer.jsp"%>
    </body>
</html>