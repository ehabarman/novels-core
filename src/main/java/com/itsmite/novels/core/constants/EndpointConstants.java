package com.itsmite.novels.core.constants;

public class EndpointConstants {

    public static final String BOOK_ID_PATH     = "/{bookId}";
    public static final String CHAPTER_ID_PATH  = "/{chapterId}";
    public static final String BOOK_ID_PARAM    = "bookId";
    public static final String CHAPTER_ID_PARAM = "chapterId";
    public static final String OWNER_ID_PARAM   = "ownerId";

    public static final String API_AUTH_V1_ENDPOINT  = "/api/auth/v1";
    public static final String API_ROLES_V1_ENDPOINT = "/api/roles/v1";

    public static final String API_BOOKS_ENDPOINT    = "/api/books";
    public static final String API_CHAPTERS_ENDPOINT = API_BOOKS_ENDPOINT + BOOK_ID_PATH + "/chapters";
}
