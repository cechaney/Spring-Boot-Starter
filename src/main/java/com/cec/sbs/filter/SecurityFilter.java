package com.cec.sbs.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.cec.sbs.security.XssUtility;

public class SecurityFilter implements Filter {

    public static final String REDIRECT_VIEW_CONFIG_PARAM = "REDIRECT_VIEW";

    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }

    @Override
    public void destroy() {
        //Do nothing on destruction
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        boolean ok = false;

        ok = XssUtility.checkRequestForXSS(request);

        if(ok){
            chain.doFilter(request, response);
        } else {

            HttpServletResponse httpResponse = (HttpServletResponse) response;

            httpResponse.sendRedirect(config.getInitParameter(REDIRECT_VIEW_CONFIG_PARAM));
        }


    }

}
