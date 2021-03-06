package com.cec.sbs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.cec.sbs.filter.InitFilter;
import com.cec.sbs.filter.SecurityFilter;

@Configuration
public class FilterConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FilterConfiguration.class);

    @Autowired
    FilterUrlPatternConfig filterPatternConfig;

    @Bean
    @Order(1)
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
    @Order(2)
    public FilterRegistrationBean getInitFilter() {

        LOGGER.debug("Creating InitFilter...");

        FilterRegistrationBean reg = new FilterRegistrationBean();

        reg.setFilter(new InitFilter());
        reg.addUrlPatterns("/*");
        reg.setName("InitFilter");

        return reg;

    }


}
