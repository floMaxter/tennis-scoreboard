<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="firstPlayerId" value="${requestScope.ongoingMatch.firstPlayer.id}"/>
<c:set var="firstPlayerName" value="${requestScope.ongoingMatch.firstPlayer.name}"/>
<c:set var="firstPlayerSetsScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.setsScore}"/>
<c:set var="firstPlayerGamesScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.gamesScore}"/>
<c:set var="firstPlayerPointsScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.pointsScore}"/>
<c:set var="firstPlayerHasAdvantage"
       value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.hasAdvantage}"/>

<c:set var="secondPlayerId" value="${requestScope.ongoingMatch.secondPlayer.id}"/>
<c:set var="secondPlayerName" value="${requestScope.ongoingMatch.secondPlayer.name}"/>
<c:set var="secondPlayerSetsScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.setsScore}"/>
<c:set var="secondPlayerGamesScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.gamesScore}"/>
<c:set var="secondPlayerPointsScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.pointsScore}"/>
<c:set var="secondPlayerHasAdvantage"
       value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.hasAdvantage}"/>

<c:set var="matchState" value="${requestScope.ongoingMatch.matchState}"/>

<html>
    <head>
        <title>Match score</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="<c:url value="/css/styles.css"/>">
        <link rel="icon" type="image/png" href="<c:url value='/images/favicon.ico'/>">
    </head>
    <body class="wrapper">
        <%@ include file="header.jsp"%>
        <main class="match-score-container">
            <h2 class="match-score-title">Match score</h2>
            <table class="score-table">
                <tr>
                    <th>Player</th>
                    <th>Sets</th>
                    <th>Games</th>
                    <th>Points</th>
                    <c:if test="${matchState != 'FINISHED'}">
                        <th>Action</th>
                    </c:if>
                </tr>
                <tr>
                    <td>${firstPlayerName}</td>
                    <td>${firstPlayerSetsScore}</td>
                    <td>${firstPlayerGamesScore}</td>
                    <td>
                        <c:choose>
                            <c:when test="${firstPlayerHasAdvantage}">AD</c:when>
                            <c:otherwise>${firstPlayerPointsScore}</c:otherwise>
                        </c:choose>
                    </td>
                    <c:if test="${matchState != 'FINISHED'}">
                        <td>
                            <form action="<c:url value="/match-score?uuid=${param.uuid}"/>" method="post">
                                <input type="hidden" name="pointWinnerId" value="${firstPlayerId}">
                                <button type="submit" class="point-button">${firstPlayerName} wins a point</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
                <tr>
                    <td>${secondPlayerName}</td>
                    <td>${secondPlayerSetsScore}</td>
                    <td>${secondPlayerGamesScore}</td>
                    <td>
                        <c:choose>
                            <c:when test="${secondPlayerHasAdvantage}">AD</c:when>
                            <c:otherwise>${secondPlayerPointsScore}</c:otherwise>
                        </c:choose>
                    </td>
                    <c:if test="${matchState != 'FINISHED'}">
                        <td>
                            <form action="<c:url value="/match-score?uuid=${param.uuid}"/>" method="post">
                                <input type="hidden" name="pointWinnerId" value="${secondPlayerId}">
                                <button type="submit" class="point-button">${secondPlayerName} wins a point</button>
                            </form>
                        </td>
                    </c:if>
                </tr>
            </table>
            <c:if test="${matchState == 'TIEBREAK'}">
                <div class="tiebreak-message">Tiebreak in progress!</div>
            </c:if>
            <br/>
            <c:if test="${matchState == 'FINISHED'}">
                <a href="<c:url value="/home" />" class="home-link-span">Home</a> <span> page</span>
            </c:if>
        </main>
        <%@ include file="footer.jsp"%>
    </body>
</html>
