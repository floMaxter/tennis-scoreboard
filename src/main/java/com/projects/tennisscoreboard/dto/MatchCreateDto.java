package com.projects.tennisscoreboard.dto;

import lombok.Builder;

@Builder
public record MatchCreateDto(String firstPlayerName, String secondPlayerName) {
}
