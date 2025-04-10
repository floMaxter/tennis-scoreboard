`<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="error" value="${requestScope.error}"/>
<c:set var="errorCode" value="${requestScope.error.code}"/>
<c:set var="errorMessage" value="${requestScope.error.message}"/>

<html lang="en">
    <head>
        <title>Error Page</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<c:url value='/css/error-page-styles.css' />">
        <link rel="icon" type="image/png" href="<c:url value='/images/favicon.ico'/>">
    </head>
    <body>
        <div class="error-container">
            <h1 class="error-title">Oops! Something went wrong.</h1>
            <p class="error-message">
                <c:if test="${not empty error}">
                    <strong>Error code:</strong> ${errorCode}
                    <br>
                    <strong>Error message: </strong> ${errorMessage}
                </c:if>
            </p>
            <div class="error-actions">
                <a href="<c:url value='/home' />" class="error-button">Go to Home</a>
                <a href="<c:url value='/matches' />" class="error-button">View Matches</a>
            </div>
        </div>
    </body>
</html>
`