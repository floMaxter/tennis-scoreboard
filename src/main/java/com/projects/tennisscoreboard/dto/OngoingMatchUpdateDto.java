package com.projects.tennisscoreboard.dto;

import lombok.Builder;

@Builder
public record OngoingMatchUpdateDto(String matchId, String pointWinnerIdStr) {
}
