package com.ht.oauth2.exceptions;

public class NoSuchClientException extends OAuth2Exception {
    public NoSuchClientException(String msg) {
        super(404,msg);
    }
}
