package com.cec.sbss.test.basic;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.cec.sbss.filter.SecurityFilter;

public class SecurityFilterTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFilterLifecycle(){

        @SuppressWarnings("unused")
        MockHttpServletRequest req = new MockHttpServletRequest();
        @SuppressWarnings("unused")
        MockHttpServletResponse res = new MockHttpServletResponse();
        @SuppressWarnings("unused")
        MockFilterChain fc = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();

        SecurityFilter filter = new SecurityFilter();

        config.addInitParameter("TEST_PARAM","true");

        try {
            filter.init(config);
        } catch (ServletException e) {
            Assert.fail();
        }

        filter.destroy();

    }

    @Test
    public void testBadParams(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();

        SecurityFilter securityFilter = new SecurityFilter();

        try{

            config.addInitParameter(SecurityFilter.REDIRECT_VIEW_CONFIG_PARAM, "error.html");

            securityFilter.init(config);

            req.addParameter("data1", "<script></script>");
            req.addParameter("data2", "<script></script> <script></script>");
            req.addParameter("data2", "");

            securityFilter.doFilter(req, res, fc);

            Assert.assertNotNull(res);
            Assert.assertTrue(HttpStatus.SC_MOVED_TEMPORARILY == res.getStatus());
            Assert.assertNotNull(res.getRedirectedUrl());


        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

    @Test
    public void testNoParams(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();

        SecurityFilter securityFilter = new SecurityFilter();

        try{

            config.addInitParameter(SecurityFilter.REDIRECT_VIEW_CONFIG_PARAM, "error.html");

            securityFilter.init(config);

            securityFilter.doFilter(req, res, fc);

            Assert.assertNotNull(res);
            Assert.assertTrue(HttpStatus.SC_OK == res.getStatus());


        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

    @Test
    public void testGoodParams(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();
        MockFilterConfig config = new MockFilterConfig();

        SecurityFilter securityFilter = new SecurityFilter();

        try{

            config.addInitParameter(SecurityFilter.REDIRECT_VIEW_CONFIG_PARAM, "error.html");

            securityFilter.init(config);

            req.addParameter("data1", "test1");
            req.addParameter("data2", "test2");

            securityFilter.doFilter(req, res, fc);

            Assert.assertFalse(HttpStatus.SC_MOVED_TEMPORARILY == res.getStatus());
            Assert.assertNull(res.getRedirectedUrl());


        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }



}
