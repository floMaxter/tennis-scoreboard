<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>New match</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
    </head>
    <body class="wrapper">
        <main class="center-container">
            <h2 class="new-match-title">New match</h2>
            <form action="<c:url value="/new-match"/>" name="players" method="post" autocomplete="on"
                  class="create-match-form">
                <label for="firstPlayerName" class="input-label">The name of the first player: </label>
                <input id="firstPlayerName" type="text" name="firstPlayerName" placeholder="Name" required class="create-input">
                <br>
                <label for="secondPlayerName" class="input-label">The name of the second player: </label>
                <input id="secondPlayerName" type="text" name="secondPlayerName" placeholder="Name" required class="create-input">
            </form>
            <br>
            <input type="submit" value="Start" class="home-button">
            <c:if test="${not empty requestScope.errors}">--%>
                <div>
                    <c:forEach var="error" items="${requestScope.errors}">
                        <span>${error.message}</span><br/>
                    </c:forEach>
                </div>
            </c:if>
        </main>
    </body>
</html>