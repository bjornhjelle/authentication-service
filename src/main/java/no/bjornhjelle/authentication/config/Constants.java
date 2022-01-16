package no.bjornhjelle.authentication.config;

/**
 * @author : github.com/sharmasourabh
 * @project : Chapter06 - Modern API Development with Spring and Spring Boot
 **/
public class Constants {
    public static final String ENCODER_ID = "bcrypt";
    public static final String API_URL_PREFIX = "/api/**";
    public static final String SIGNUP_URL = "/api/users";
    public static final String TOKEN_URL = "/api/auth/token";
    public static final String REFRESH_URL = "/api/auth/token/refresh";
    public static final String PRODUCTS_URL = "/api/products/**";
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String SECRET_KEY = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 900_000; // 15 mins
    public static final String ROLE_CLAIM = "roles";
    public static final String AUTHORITY_PREFIX = "ROLE_";
}
