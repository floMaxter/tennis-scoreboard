package com.projects.tennisscoreboard.mapper;

import com.projects.tennisscoreboard.dto.MatchCreateDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class MatchCreateMapper implements Mapper<HttpServletRequest, MatchCreateDto> {

    private static final MatchCreateMapper INSTANCE = new MatchCreateMapper();

    private MatchCreateMapper() {
    }

    @Override
    public MatchCreateDto mapFrom(HttpServletRequest object) {
        return new MatchCreateDto(normalizeName(
                object.getParameter("firstPlayerName")),
                normalizeName(object.getParameter("secondPlayerName"))
        );
    }

    private String normalizeName(String name) {
        return Optional.ofNullable(name)
                .map(String::trim)
                .map(str -> str.replaceAll("\\s+", " "))
                .orElse("");
    }

    public static MatchCreateMapper getInstance() {
        return INSTANCE;
    }
}
