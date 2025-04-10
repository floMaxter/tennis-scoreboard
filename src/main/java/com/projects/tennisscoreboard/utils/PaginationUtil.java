package com.projects.tennisscoreboard.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PaginationUtil {

    public static final Integer RECORDS_PER_PAGE = Integer.parseInt(PropertiesUtil.get("pagination.matches_per_page"));
}
