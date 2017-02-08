package com.cec.sbs.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "filter-url-patterns")
public class FilterUrlPatternConfig {

    private List<String> securedEndpointFilterPatterns;

    public List<String> getSecuredEndpointFilterPatterns() {
        return securedEndpointFilterPatterns;
    }

    public void setSecuredEndpointFilterPatterns(List<String> securedEndpointFilterPatterns) {
        this.securedEndpointFilterPatterns = securedEndpointFilterPatterns;
    }

}
