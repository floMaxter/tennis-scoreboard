package com.projects.tennisscoreboard.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MatchLockManager {

    private static final Map<String, ReentrantReadWriteLock> locks = new ConcurrentHashMap<>();



    public static ReentrantReadWriteLock getLock(String matchId) {
        return locks.computeIfAbsent(matchId, id -> new ReentrantReadWriteLock());
    }

    public static void removeLock(String matchId) {
        locks.remove(matchId);
    }
}
