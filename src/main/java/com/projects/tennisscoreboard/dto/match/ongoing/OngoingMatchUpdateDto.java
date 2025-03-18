package com.projects.tennisscoreboard.dto.match.ongoing;

import lombok.Builder;

@Builder
public record OngoingMatchUpdateDto(String matchId, String pointWinnerIdStr) {
}
