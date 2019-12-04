package br.com.devdojo.config;

import org.springframework.security.core.Authentication;

import java.util.concurrent.TimeUnit;

/**
 * Created by Thiago Rodrigues on 03/12/2019
 */
public class SecurityConstants {
    // Authentication Bearer dsdsdsdsdsd
    static final String SECRET = "DevDojoFoda";
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/users/sign-up";
    static final long   EXPIRATION_TIME = 86400000;




    /* // metodo que descore a quantidade de milesegundos tem um dia
    public static void main(String[] args) {
        System.out.println(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    }*/

}
