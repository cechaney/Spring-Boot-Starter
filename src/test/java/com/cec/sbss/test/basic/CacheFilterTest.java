package com.cec.sbss.test.basic;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.cec.sbss.filter.CacheFilter;

public class CacheFilterTest {

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
    public void testCacheFilterCachableRequest(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();

        req.setRequestURI("/first/second/1");
        req.addHeader(CacheFilter.ESI_CAPABLE_HEADER, "true");

        CacheFilter cacheFilter = new CacheFilter();

        try{

            cacheFilter.doFilter(req, res, fc);

            Assert.assertNotNull(res.getHeader(CacheFilter.X_VARNISH_ESI));
            Assert.assertTrue("true" == res.getHeader(CacheFilter.X_VARNISH_ESI));


            Assert.assertNotNull(req.getAttribute(CacheFilter.VARNISH_ENABLED));
            Assert.assertTrue("true" == req.getAttribute(CacheFilter.VARNISH_ENABLED));

            Assert.assertNotNull(res.getHeader(CacheFilter.CACHE_CONTROL_HEADER));
            Assert.assertTrue(CacheFilter.S_MAX_AGE_ONE_DAY == res.getHeader(CacheFilter.CACHE_CONTROL_HEADER));

            cacheFilter.destroy();

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

    @Test
    public void testCacheFilterIsCacheablePrivateMethod(){

        MockHttpServletRequest mockReq = new MockHttpServletRequest();

        CacheFilter cacheFilter = new CacheFilter();

        try {

            Object result = null;

            Method isCacheableMethod = cacheFilter.getClass().getDeclaredMethod("isCachable", HttpServletRequest.class);

            isCacheableMethod.setAccessible(true);

            result = isCacheableMethod.invoke(cacheFilter, mockReq);

            Assert.assertTrue((boolean)result);

            mockReq.setParameter("badparam", "false");

            result = isCacheableMethod.invoke(cacheFilter, mockReq);

            Assert.assertTrue((boolean)result);

            mockReq.setParameter("pack", "false");

            result = isCacheableMethod.invoke(cacheFilter, mockReq);

            Assert.assertFalse((boolean)result);


        } catch (NoSuchMethodException |
                SecurityException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException e) {

            e.printStackTrace();
            Assert.fail();
        }

    }

    @Test
    public void testCacheFilterNonCachableRequestNoHeader(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();

        CacheFilter cacheFilter = new CacheFilter();

        try{

            cacheFilter.doFilter(req, res, fc);

            Assert.assertNull(res.getHeader(CacheFilter.X_VARNISH_ESI));
            Assert.assertNull(req.getAttribute(CacheFilter.VARNISH_ENABLED));

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

    @Test
    public void testCacheFilterNonCachableRequestFalseHeader(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();

        CacheFilter cacheFilter = new CacheFilter();

        req.addHeader(CacheFilter.ESI_CAPABLE_HEADER, "false");

        try{

            cacheFilter.doFilter(req, res, fc);

            Assert.assertNull(res.getHeader(CacheFilter.X_VARNISH_ESI));
            Assert.assertNull(req.getAttribute(CacheFilter.VARNISH_ENABLED));

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

    @Test
    public void testNoPathParams(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();

        CacheFilter cacheFilter = new CacheFilter();

        req.setRequestURI("");

        try{

            cacheFilter.doFilter(req, res, fc);

            Assert.assertNull(res.getHeader(CacheFilter.X_VARNISH_ESI));
            Assert.assertNull(req.getAttribute(CacheFilter.VARNISH_ENABLED));

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

    @Test
    public void testBadMatchPathParams(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();

        CacheFilter cacheFilter = new CacheFilter();

        req.setRequestURI("/first/second/bad");

        try{

            cacheFilter.doFilter(req, res, fc);

            Assert.assertNull(res.getHeader(CacheFilter.X_VARNISH_ESI));
            Assert.assertNull(req.getAttribute(CacheFilter.VARNISH_ENABLED));

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

    @Test
    public void testCacheBusting(){

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain fc = new MockFilterChain();

        CacheFilter cacheFilter = new CacheFilter();

        req.setParameter("pack", "false");

        try{

            cacheFilter.doFilter(req, res, fc);

            Assert.assertNull(res.getHeader(CacheFilter.X_VARNISH_ESI));
            Assert.assertNull(req.getAttribute(CacheFilter.VARNISH_ENABLED));
            Assert.assertNull(res.getHeader(CacheFilter.CACHE_CONTROL_HEADER));

        } catch(IOException | ServletException ex){
            Assert.fail();
        }

    }

}
