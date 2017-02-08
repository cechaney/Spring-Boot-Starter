package com.cec.sbs.test.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cec.sbs.config.Configurator;
import com.cec.sbs.health.ConfigHealthIndicator;

public class ConfiguratorTest {

    private final Logger log = LoggerFactory.getLogger(ConfiguratorTest.class);

    private Path tempDir;
    private Properties initialProperties;

    private ConfigHealthIndicator healthIndicator = new ConfigHealthIndicator();

    private static final String TEST_PROP = "TEST_PROP";

    @Before
    public void setup() throws Exception {

    	//Create a temp dir for to make config changes to
        tempDir = Files.createTempDirectory("configFactory");

        initialProperties = new Properties();

        try (
                InputStream is = this.getClass().getResourceAsStream("/config.properties");
                OutputStream out = Files.newOutputStream(Paths.get(tempDir.toString(), "config.properties"));
        ) {
            initialProperties.load(is);
            initialProperties.store(out, "Default configuration from resources");

        }
    }

    @After
    public void cleanup() {

    }

    private void compareProps(Properties p1, Properties p2) {
        compareProps(p1, p2, true);
    }

    private void compareProps(Properties expected, Properties actual, boolean shouldMatch) {

        //Get all the keys from p1 and ensure they're all in p2, exactly, no extras
        log.debug("comparing props: \nexpected {}\n  actual {}", expected, actual);

        Set<Object> expectedKeys = expected.keySet();
        Set<Object> actualKeys = actual.keySet();

        if (shouldMatch) {
            assertEquals("Same number of keys in actual as expected", expectedKeys.size(), actualKeys.size());
        }

        for (Object o : expectedKeys) {
            assertTrue("contains key: " + o, actualKeys.contains(o));
        }

        //All the keys are the same and are there -- verify that all the values match
        for (Object o : expectedKeys) {
            assertEquals("parameter " + o + " matches", expected.get(o), actual.get(o));
        }
    }


    @Test
    public void verifyPicksUpChanges() throws Exception {

        Configurator cf = new Configurator(
        		Paths.get(
        				tempDir.toString(), "config.properties").toString(),
        				250,
        				healthIndicator);

        //Ensure it loaded the current version of the properties and that they match
        Properties p = cf.getRequestConfig();

        compareProps(p, initialProperties);

        //Grab a copy of the original props before we modify it
        Properties original = new Properties();

        original.putAll(initialProperties);

        log.debug("compared initial properties");

        cf.start();

        Thread.sleep(500); //Wait for the configurator to start watching

        try (FileOutputStream out = new FileOutputStream(Paths.get(tempDir.toString(), "config.properties").toString());) {

            initialProperties.setProperty("another.value", "XOXOXOXO");
            initialProperties.setProperty("LOGLEVEL.com.cec.sbs.service", "ERROR");
            initialProperties.store(out, "Default configuration from resources");

            out.flush();
            out.getFD().sync();

        }

        //Wait a while to pick up on the changes
        Thread.sleep(500);

        log.debug("asking for a new config and comparing it");

        //ensure it loaded the current version of the properties and that they match
        Properties p2 = cf.getRequestConfig();
        compareProps(initialProperties, p2);

        //also ensure that the old version of the properties does not match
        assertNull("original should not contain our new property", original.getProperty("another.value"));

        //Shut it down
        cf.shutdown();
    }

    @Test
    public void testSetCurrrentConfig() throws IOException{

    	Configurator cf = new Configurator(
    			Paths.get(tempDir.toString(), "config.properties").toString(),
    			250,
    			healthIndicator);

    	Properties beforeProps = cf.getGlobalConfig();

    	assertNull(beforeProps.get(TEST_PROP));

    	Properties propsDelta = new Properties();

    	propsDelta.putAll(beforeProps);

    	propsDelta.put(TEST_PROP, "hello");

    	cf.setGlobalConfig(propsDelta);

    	Properties afterProps = cf.getGlobalConfig();

    	assertNotNull(afterProps.get(TEST_PROP));


    }

    @Test
    public void testDefaultConfigThatGoesBad() throws IOException{

    	Configurator cf = new Configurator(
    			"",
    			250,
    			healthIndicator);

    	cf.shutdown();

    }
}