package com.cec.sbss.test.basic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.cec.sbss.config.ConfigConstants;

public class ConfigConstantsTest {

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

        Constructor<ConfigConstants> c = null;

        try {
            c = ConfigConstants.class.getDeclaredConstructor();
        } catch (NoSuchMethodException | SecurityException e) {
            Assert.fail();
        }

        c.setAccessible(true);

        try {
            @SuppressWarnings("unused")
            ConfigConstants u = c.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            Assert.fail();
        }
    }

}
