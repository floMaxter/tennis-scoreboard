<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="firstPlayerId" value="${requestScope.ongoingMatch.firstPlayer.id}"/>
<c:set var="firstPlayerName" value="${requestScope.ongoingMatch.firstPlayer.name}"/>
<c:set var="firstPlayerSetsScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.setsScore}"/>
<c:set var="firstPlayerGamesScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.gamesScore}"/>
<c:set var="firstPlayerPointsScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.pointsScore}"/>
<c:set var="firstPlayerAdvantagePointsScore" value="${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.advantagePointScore}"/>

<c:set var="secondPlayerId" value="${requestScope.ongoingMatch.secondPlayer.id}"/>
<c:set var="secondPlayerName" value="${requestScope.ongoingMatch.secondPlayer.name}"/>
<c:set var="secondPlayerSetsScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.setsScore}"/>
<c:set var="secondPlayerGamesScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.gamesScore}"/>
<c:set var="secondPlayerPointsScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.pointsScore}"/>
<c:set var="secondPlayerAdvantagePointsScore" value="${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.advantagePointScore}"/>

<c:set var="matchState" value="${requestScope.ongoingMatch.matchState}"/>

<html>
    <head>
        <title>Match Score</title>
    </head>
    <body>
        <h1>Match Score</h1>
        <h2>Match State: ${requestScope.ongoingMatch.matchState}</h2>
        <h2>First player advantage point score: ${firstPlayerAdvantagePointsScore}</h2>
        <h2>Second player advantage point score: ${secondPlayerAdvantagePointsScore}</h2>
        <table>
            <tr>
                <td>Player</td>
                <td>Sets</td>
                <td>Games</td>
                <td>Points</td>
            </tr>
            <tr>
                <td>${firstPlayerName}</td>
                <td>${firstPlayerSetsScore}</td>
                <td>${firstPlayerGamesScore}</td>
                <td>${firstPlayerPointsScore}</td>
                <c:if test="${matchState != 'FINISHED'}">
                    <td>
                        <form action="<c:url value="/match-score?uuid=${param.uuid}"/>" method="post">
                            <input type="hidden" name="pointWinnerId" value="${firstPlayerId}">
                            <button type="submit">${firstPlayerName} wins a point</button>
                        </form>
                    </td>
                </c:if>
            </tr>
            <tr>
                <td>${secondPlayerName}</td>
                <td>${secondPlayerSetsScore}</td>
                <td>${secondPlayerGamesScore}</td>
                <td>${secondPlayerPointsScore}</td>
                <c:if test="${matchState != 'FINISHED'}">
                    <td>
                        <form action="<c:url value="/match-score?uuid=${param.uuid}"/>" method="post">
                            <input type="hidden" name="pointWinnerId" value="${secondPlayerId}">
                            <button type="submit">${secondPlayerName} wins a point</button>
                        </form>
                    </td>
                </c:if>
            </tr>
        </table>
        <br/>
        <c:if test="${matchState == 'FINISHED'}">
            <a href="<c:url value="/home" />">Home</a> <span> page</span>
        </c:if>
    </body>
</html>
