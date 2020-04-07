package com.ht.oauth2.exceptions;

public class InvalidGrantException extends OAuth2Exception {
    public InvalidGrantException(String msg) {
        super(401, msg);
    }
}
