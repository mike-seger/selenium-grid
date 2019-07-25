package com.net128.test.selenium;

import junit.framework.TestCase;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.*;
import java.net.URL;

public class RemoteWebDriverTest extends TestCase {
    private ChromeOptions chromeOptions = new ChromeOptions();
    private FirefoxOptions firefoxOptions = new FirefoxOptions();
    private RemoteWebDriver chrome;
    private RemoteWebDriver firefox;

    public void setUp() throws Exception {
        chrome = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeOptions);
        firefox = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefoxOptions);
    }

    public void testChrome() {
        chrome.get("http://www.google.com");
        assertEquals("Google", chrome.getTitle());
    }

    public void testFirefox() {
        firefox.get("http://www.google.com");
        assertEquals("Google", firefox.getTitle());
    }

    public void tearDown() {
        chrome.quit();
        firefox.quit();
    }
}

