package com.cec.sbs.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class CacheFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(CacheFilter.class);

    public static final String X_VARNISH_ESI = "X-VARNISH-ESI";
    public static final String VARNISH_ENABLED = "VARNISH_ENABLED";
    public static final String ESI_CAPABLE_HEADER = "X-ESI-CAPABLE";
    public static final String ESI_CAPABLE_HEADER_VALUE = "true";
    public static final String REGEX_MATCH_DIGITS = "^[0-9]+$";
    public static final String S_MAX_AGE_ONE_DAY = "s-maxage=86400s";
    public static final String CACHE_CONTROL_HEADER = "Cache-Control";

    private static final List<String> NON_CACHE_PARAMS = new ArrayList<String>(
            Arrays.asList(
                    new String[]{"pack", "packRegenerate", "min", "debug"}));


    protected FilterConfig config;

    @Override
    public void init(final FilterConfig config) throws ServletException {
        this.config = config;
    }

    @Override
    public void destroy() {
        // Do nothing on destruction
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (StringUtils.isNotBlank(httpRequest.getHeader(ESI_CAPABLE_HEADER))
                && ESI_CAPABLE_HEADER_VALUE.equalsIgnoreCase(
                        httpRequest.getHeader(ESI_CAPABLE_HEADER))){

            httpResponse.setHeader(X_VARNISH_ESI, "true");
            httpRequest.setAttribute(VARNISH_ENABLED, "true");

        }

        if (isCachable(httpRequest)) {

            String path = httpRequest.getRequestURI();
            String[] pathParams = StringUtils.split(path, "/");

            if (pathParams.length == 3 && pathParams[2].matches(REGEX_MATCH_DIGITS)) {

                LOGGER.debug(" Request cacheable");

                httpResponse.setHeader(CACHE_CONTROL_HEADER, S_MAX_AGE_ONE_DAY);

            }

        }

        chain.doFilter(request, response);

    }

    /**
     * Check if the request parameters fall in the list of parameters that can
     * override cache
     *
     * @param httpRequest
     */
    private boolean isCachable(HttpServletRequest httpRequest) {

        List<String> reqParams = Collections.list(httpRequest.getParameterNames());

        if(CollectionUtils.intersection(reqParams, NON_CACHE_PARAMS).size() > 0){
            return false;
        } else {
            return true;
        }

    }

}
