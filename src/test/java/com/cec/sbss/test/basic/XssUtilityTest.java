package com.cec.sbss.test.basic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cec.sbss.security.XssUtility;

public class XssUtilityTest {

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
    public void testConstuctor(){

        Constructor<XssUtility> c = null;

        try {
            c = XssUtility.class.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            Assert.fail();
        }

        c.setAccessible(true);

        try {
            @SuppressWarnings("unused")
            XssUtility u = c.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            Assert.fail();
        }
    }

    @Test
    public void isSafeTest() {

        //Test single bad strings
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("<a></a>")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("<img></img>")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("<Script></Script>")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("<script></script>")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("javascript.alert")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("<%></%>")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("onload")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("alert(")));

        //Test multiple bad strings
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("onload alert(")));
        Assert.assertFalse(XssUtility.isSafe(StringEscapeUtils.escapeHtml4("<script></script> <script></script>")));

    }

}
