package com.cec.sbs.health;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HealthCheckEndpoint {

	@Autowired
    private HealthEndpoint healthEndpoint;

    @RequestMapping("/health")
    public void healthCheck(HttpServletResponse res) {

        if (healthEndpoint.invoke().getStatus().equals(Status.UP)) {
            res.setStatus(HttpStatus.SC_OK);
        } else {
            res.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }

    }

}
