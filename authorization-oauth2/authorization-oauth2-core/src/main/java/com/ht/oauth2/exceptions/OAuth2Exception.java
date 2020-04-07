package com.ht.oauth2.exceptions;

public class OAuth2Exception extends RuntimeException {
    private int code;

    public OAuth2Exception(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
