<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
    <head>
        <title>Tennis scoreboard</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="<c:url value="/css/styles.css"/>"/>
        <link rel="icon" type="image/png" href="<c:url value='/images/favicon.ico'/>">
    </head>
    <body class="wrapper">
        <main class="center-container">
            <h2 class="welcome-text">Welcome to the Tennis scoreboard app</h2>
            <div class="home-button-container">
                <a href="<c:url value="/new-match"/>" class="primary-button">New match</a>
                <a href="<c:url value="/matches"/>" class="primary-button">Completed matches</a>
            </div>
        </main>
        <%@ include file="footer.jsp"%>
    </body>
</html>