<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>New match</title>
    </head>
    <body>
        <h1><c:out value="New Match"/></h1>
        <form action="<c:url value="/new-match"/>" method="post">
            <label for="firstPlayerName">
                First player name:
                <input type="text" name="firstPlayerName" id="firstPlayerName"/>
            </label><br>
            <label for="secondPlayerName">
                Second player name:
                <input type="text" name="secondPlayerName" id="secondPlayerName"/>
            </label><br>
            <button type="submit">Start</button>
        </form>
        <c:if test="${not empty requestScope.errors}">
            <div>
                <c:forEach var="error" items="${requestScope.errors}">
                    <span>${error.message}</span><br/>
                </c:forEach>
            </div>
        </c:if>
    </body>
</html>
