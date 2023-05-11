package com.mobisem.kasra.todoapp.security;

public class SecurityConstants {

    public static final String SIGN_UP_URL = "/users/register";
    public static final String SECRET = "mobisem";
    public static final long EXPIRATION_TIME = 432_000_000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
