<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Match Score</title>
    </head>
    <body>
        <h1>Match Score</h1>
        <table>
            <tr>
                <td>Player</td>
                <td>Sets</td>
                <td>Games</td>
                <td>Points</td>
            </tr>
            <tr>
                <td>${requestScope.ongoingMatch.firstPlayer.name}</td>
                <td>${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.setsScore}</td>
                <td>${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.gamesScore}</td>
                <td>${requestScope.ongoingMatch.matchScoreDto.firstPlayerScore.pointsScore}</td>
                <td>
                    <form action="<c:url value="/match-score?uuid=${param.uuid}"/>" method="post">
                        <input type="hidden" name="pointWinnerId" value="${requestScope.ongoingMatch.firstPlayer.id}">
                        <button type="submit">${requestScope.ongoingMatch.firstPlayer.name} wins a point</button>
                    </form>
                </td>
            </tr>
            <tr>
                <td>${requestScope.ongoingMatch.secondPlayer.name}</td>
                <td>${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.setsScore}</td>
                <td>${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.gamesScore}</td>
                <td>${requestScope.ongoingMatch.matchScoreDto.secondPlayerScore.pointsScore}</td>
                <td>
                    <form action="<c:url value="/match-score?uuid=${param.uuid}"/>" method="post">
                        <input type="hidden" name="pointWinnerId" value="${requestScope.ongoingMatch.secondPlayer.id}">
                        <button type="submit">${requestScope.ongoingMatch.secondPlayer.name} wins a point</button>
                    </form>
                </td>
            </tr>
        </table>
    </body>
</html>
