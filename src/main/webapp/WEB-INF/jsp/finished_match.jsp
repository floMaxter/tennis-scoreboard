<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="firstPlayerName" value="${requestScope.ongoingMatch.firstPlayer.name}"/>
<c:set var="firstPlayerSetsScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.setsScore}"/>
<c:set var="firstPlayerGamesScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.gamesScore}"/>
<c:set var="firstPlayerPointsScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.pointsScore}"/>

<c:set var="secondPlayerName" value="${requestScope.ongoingMatch.secondPlayer.name}"/>
<c:set var="secondPlayerSetsScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.setsScore}"/>
<c:set var="secondPlayerGamesScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.gamesScore}"/>
<c:set var="secondPlayerPointsScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.pointsScore}"/>

<c:set var="winner" value="${requestScope.winner.name}"/>

<html>
    <head>
        <title>Finished match</title>
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
                </tr>
                <tr>
                    <td>${firstPlayerName}</td>
                    <td>${firstPlayerSetsScore}</td>
                    <td>${firstPlayerGamesScore}</td>
                    <td>${firstPlayerPointsScore}</td>
                </tr>
                <tr>
                    <td>${secondPlayerName}</td>
                    <td>${secondPlayerSetsScore}</td>
                    <td>${secondPlayerGamesScore}</td>
                    <td>${secondPlayerPointsScore}</td>
                </tr>
            </table>
            <div class="winner-player-message">${winner} win the match!</div>
            <form action="<c:url value="/finished-match?uuid=${param.uuid}"/>" method="post" class="save-match-form">
                <button type="submit" class="save-match-button">Save the match and go to the home page</button>
            </form>
        </main>
        <%@ include file="footer.jsp"%>
    </body>
</html>
