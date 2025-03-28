package com.projects.tennisscoreboard.dto.match.ongoing;

import lombok.Builder;

@Builder
public record MatchCreateDto(String firstPlayerName, String secondPlayerName) {
}
