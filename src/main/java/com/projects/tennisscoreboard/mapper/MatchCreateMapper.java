package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.MatchCreateDto;
import jakarta.servlet.http.HttpServletRequest;

public class MatchCreateMapper implements Mapper<HttpServletRequest, MatchCreateDto> {

    private static final MatchCreateMapper INSTANCE = new MatchCreateMapper();

    private MatchCreateMapper() {
    }

    @Override
    public MatchCreateDto mapFrom(HttpServletRequest object) {
        return new MatchCreateDto(object.getParameter("firstPlayerName"),
                object.getParameter("secondPlayerName"));
    }

    public static MatchCreateMapper getInstance() {
        return INSTANCE;
    }
}
