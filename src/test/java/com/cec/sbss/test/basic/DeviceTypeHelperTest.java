package com.cec.sbss.test.basic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.cec.sbss.device.DeviceTypeHelper;

public class DeviceTypeHelperTest {

    private static final String TEST_AGENT = "test-agent";
    private static final String ANDROID_AGENT = "Android Mobile";
    private static final String IPHONE_AGENT = "iPhone iPod";
    private static final String BLACKBERRY_AGENT = "BlackBerry";
    private static final String WINDOWS_PHONE_AGENT = "Windows Phone";

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

        Constructor<DeviceTypeHelper> c = null;

        try {
            c = DeviceTypeHelper.class.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            Assert.fail();
        }

        c.setAccessible(true);

        try {
            @SuppressWarnings("unused")
            DeviceTypeHelper u = c.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            Assert.fail();
        }
    }

    @Test
    public void getUserAgentTest(){

        MockHttpServletRequest mtsr = new MockHttpServletRequest();

        mtsr.addHeader("User-Agent", TEST_AGENT);

        Assert.assertEquals(TEST_AGENT, DeviceTypeHelper.getUserAgent(mtsr));

        Assert.assertNull(TEST_AGENT, DeviceTypeHelper.getUserAgent(null));

    }

    @Test
    public void isAndroidTest(){

        MockHttpServletRequest mtsr = new MockHttpServletRequest();

        mtsr.addHeader("User-Agent", ANDROID_AGENT);

        Assert.assertTrue(DeviceTypeHelper.isAndroid(mtsr));

        Assert.assertFalse(DeviceTypeHelper.isAndroid(null));

    }

    @Test
    public void isIPhoneTest(){

        MockHttpServletRequest mtsr = new MockHttpServletRequest();

        mtsr.addHeader("User-Agent", IPHONE_AGENT);

        Assert.assertTrue(DeviceTypeHelper.isIphone(mtsr));

        Assert.assertFalse(DeviceTypeHelper.isIphone(null));

    }

    @Test
    public void isBlackberryTest(){

        MockHttpServletRequest mtsr = new MockHttpServletRequest();

        mtsr.addHeader("User-Agent", BLACKBERRY_AGENT);

        Assert.assertTrue(DeviceTypeHelper.isBlackberry(mtsr));

        Assert.assertFalse(DeviceTypeHelper.isBlackberry(null));

    }

    @Test
    public void isWindowsPhoneTest(){

        MockHttpServletRequest mtsr = new MockHttpServletRequest();

        mtsr.addHeader("User-Agent", WINDOWS_PHONE_AGENT);

        Assert.assertTrue(DeviceTypeHelper.isWindowsPhone(mtsr));

        Assert.assertFalse(DeviceTypeHelper.isWindowsPhone(null));

    }

}
