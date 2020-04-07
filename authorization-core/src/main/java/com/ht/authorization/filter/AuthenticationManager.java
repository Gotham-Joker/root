package com.ht.authorization.filter;

import com.ht.authorization.Authentication;
import com.ht.authorization.exception.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AuthenticationManager {
    Authentication authenticate(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException;
}
