package com.net128.test.selenium;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class RemoteWebDriverTest extends TestCase {
    private ChromeOptions chromeOptions = new ChromeOptions();
    private FirefoxOptions firefoxOptions = new FirefoxOptions();
    private RemoteWebDriver chrome;
    private RemoteWebDriver firefox;
    private Map<String,String> configuration;
    private static String configName="configuration.json";

    public void setUp() throws Exception {
        ObjectMapper mapper=new ObjectMapper();
        configuration = mapper.readValue(getClass()
            .getResource(configName), HashMap.class);
        try (FileInputStream fis=new FileInputStream(configName))
            { mapper.readerForUpdating(configuration).readValue(fis); }
        catch(FileNotFoundException e) {}
        catch(Exception e) { e.printStackTrace(); }
        chrome = new RemoteWebDriver(new URL(configuration.get("hubBaseUrl")+"/wd/hub"), chromeOptions);
        firefox = new RemoteWebDriver(new URL(configuration.get("hubBaseUrl")+"/wd/hub"), firefoxOptions);
    }

    public void testChrome() {
        chrome.get(configuration.get("homePage"));
        assertEquals(configuration.get("expectedTitle"), chrome.getTitle());
    }

    public void testFirefox() {
        firefox.get(configuration.get("homePage"));
        assertEquals(configuration.get("expectedTitle"), firefox.getTitle());
    }

    public void tearDown() {
        chrome.quit();
        firefox.quit();
    }
}

