package br.com.rocksti.ofmanager.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String GESTOR_OF = "ROLE_GESTOR_OF";

    private AuthoritiesConstants() {
    }
}
