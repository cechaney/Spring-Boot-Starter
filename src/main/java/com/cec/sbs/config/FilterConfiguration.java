package com.cec.sbs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.cec.sbs.filter.CacheFilter;
import com.cec.sbs.filter.InitFilter;
import com.cec.sbs.filter.SecurityFilter;
import com.cec.sbs.filter.StaticAssetFilter;

@Configuration
public class FilterConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterConfiguration.class);

    @Autowired
    FilterUrlPatternConfig filterPatternConfig;

    @Bean
    @Order(2)
    public FilterRegistrationBean getStaticAssetFilter() {

        LOGGER.debug("Creating StaticAssetFilter...");

        FilterRegistrationBean reg = new FilterRegistrationBean();

        reg.setFilter(new StaticAssetFilter());
        reg.addUrlPatterns("/*");
        reg.setName("StaticAssetFilter");

        return reg;

    }

    @Bean
    @Order(3)
    public FilterRegistrationBean getSecurityFilter() {

        LOGGER.debug("Creating SecurityFilter...");

        FilterRegistrationBean reg = new FilterRegistrationBean();

        reg.setFilter(new SecurityFilter());
        reg.addInitParameter(SecurityFilter.REDIRECT_VIEW_CONFIG_PARAM, "/error.html");
        reg.setUrlPatterns(filterPatternConfig.getSecuredEndpointFilterPatterns());
        reg.setName("SecurityFilter");

        return reg;

    }


    @Bean
    @Order(4)
    public FilterRegistrationBean getCacheFilter() {

        LOGGER.debug("Creating CacheFilter...");

        FilterRegistrationBean reg = new FilterRegistrationBean();

        reg.setFilter(new CacheFilter());
        reg.addUrlPatterns("/*");
        reg.setName("CacheFilter");

        return reg;

    }

    @Bean
    @Order(5)
    public FilterRegistrationBean getInitFilter() {

        LOGGER.debug("Creating InitFilter...");

        FilterRegistrationBean reg = new FilterRegistrationBean();

        reg.setFilter(new InitFilter());
        reg.addUrlPatterns("/*");
        reg.setName("InitFilter");

        return reg;

    }


}
