package com.epam.restaurant.constants;


import org.apache.http.HttpStatus;

public class StatusCodes {
    public static final int SUCCESS_CREATED = HttpStatus.SC_CREATED;
    public static final int SUCCESS_OK = 200;
    public static final int BAD_REQUEST = 400;
    public static final int CONFLICT = 409;
    public static final int UNAUTHORIZED = 401;
    public static final int FORBIDDEN = 403;
    public static final int NOT_FOUND = 404;
    public static final int METHOD_NOT_ALLOWED = 405;
    public static final int SERVER_ERROR = 500;
}
