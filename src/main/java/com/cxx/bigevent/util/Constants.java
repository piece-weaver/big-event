package com.cxx.bigevent.util;

public class Constants {
    private Constants() {}

    public static final long TOKEN_EXPIRATION_MS = 6 * 60 * 60 * 1000L;

    public static final String HOT_ARTICLES_KEY = "recommendation:hot";
    public static final int CACHE_MINUTES = 30;

    public static final String REDIS_HOST = "localhost";
    public static final int REDIS_PORT = 6379;

    public static final String OLLAMA_HOST = "http://localhost:11434";
    public static final String OLLAMA_MODEL = "deepseek-r1:8b";

    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final int MAX_PAGE_SIZE = 100;
}
