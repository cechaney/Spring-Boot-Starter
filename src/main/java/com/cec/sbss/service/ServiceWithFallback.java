package com.cec.sbss.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cec.sbss.client.ClientWithFallback;
import com.cec.sbss.client.ClientWithFallback.ClientException;

@RestController
@RequestMapping("/fallback")
public class ServiceWithFallback {

    private static final Logger LOGGER = Logger.getLogger(ServiceWithFallback.class);

    @Autowired
    ClientWithFallback fallbackClient;

    public ClientWithFallback getFallbackClient() {
        return fallbackClient;
    }

    public void setFallbackClient(ClientWithFallback fallbackClient) {
        this.fallbackClient = fallbackClient;
    }

    @RequestMapping(value = "/no", method = RequestMethod.GET)
    public ResponseEntity<String> noFallback() {

        return fallbackClient.noFallback();

    }

    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public ResponseEntity<String> exceptionFallback(boolean runClean) {

        try {
            return fallbackClient.exceptionFallback(runClean);
        } catch (ClientException ce) {
            LOGGER.info("Call to exception fallback failed", ce);
            return null;
        }

    }

    @RequestMapping(value = "/timeout", method = RequestMethod.GET)
    public ResponseEntity<String> timeoutFallback() {

        return fallbackClient.timeoutFallback();

    }

}
