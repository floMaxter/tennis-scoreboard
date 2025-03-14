package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.OngoingMatchUpdateDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class OngoingMatchUpdateMapper implements Mapper<HttpServletRequest, OngoingMatchUpdateDto> {

    private static final OngoingMatchUpdateMapper INSTANCE = new OngoingMatchUpdateMapper();

    private OngoingMatchUpdateMapper() {
    }

    @Override
    public OngoingMatchUpdateDto mapFrom(HttpServletRequest object) {
        return OngoingMatchUpdateDto.builder()
                .matchId(normalizeParameter(object.getParameter("uuid")))
                .pointWinnerIdStr(normalizeParameter(object.getParameter("pointWinnerId")))
                .build();
    }

    private String normalizeParameter(String param) {
        return Optional.ofNullable(param)
                .orElse("");
    }

    public static OngoingMatchUpdateMapper getInstance() {
        return INSTANCE;
    }
}
