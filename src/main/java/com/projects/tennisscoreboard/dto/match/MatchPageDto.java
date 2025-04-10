package com.projects.tennisscoreboard.dto.match;


import com.projects.tennisscoreboard.dto.match.completed.MatchReadDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MatchPageDto {

    private Integer currentPage;
    private Long totalPages;
    private List<MatchReadDto> matches;
}
