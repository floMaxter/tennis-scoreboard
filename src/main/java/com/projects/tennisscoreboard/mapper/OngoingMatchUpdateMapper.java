package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.OngoingMatchUpdateDto;
import jakarta.servlet.http.HttpServletRequest;

public class OngoingMatchUpdateMapper implements Mapper<HttpServletRequest, OngoingMatchUpdateDto> {

    private static final OngoingMatchUpdateMapper INSTANCE = new OngoingMatchUpdateMapper();

    private OngoingMatchUpdateMapper() {
    }

    @Override
    public OngoingMatchUpdateDto mapFrom(HttpServletRequest object) {
        return OngoingMatchUpdateDto.builder()
                .matchId(object.getParameter("uuid"))
                .pointWinnerIdStr(object.getParameter("pointWinnerId"))
                .build();
    }

    public static OngoingMatchUpdateMapper getInstance() {
        return INSTANCE;
    }
}
