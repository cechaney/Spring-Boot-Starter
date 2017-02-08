package com.cec.sbss.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class StaticAssetFilter implements Filter {

    protected FilterConfig config;

    private static final String SPRING_DISPATCHER_NAME = "dispatcherServlet";
    private static final String STATIC_REGEX = ".*.(css|js|bmp|jpg|jpeg|ico|png|gif|html|id|txt|svg|pdf|woff2|woff|eot|xml)";

    private RequestDispatcher dispatcher;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        this.dispatcher =  filterConfig.getServletContext().getNamedDispatcher(SPRING_DISPATCHER_NAME);
    }

    @Override
    public void destroy() {
        //Do nothing on destruction
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        if(req.getRequestURI().matches(STATIC_REGEX)){
            dispatcher.forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

}