package com.net128.test.webdriver;

import junit.framework.TestCase;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import java.net.URL;

public class WebDriverTest extends TestCase {
    private Capabilities chromeCapabilities = DesiredCapabilities.chrome();
    private Capabilities firefoxCapabilities = DesiredCapabilities.firefox();
    private RemoteWebDriver chrome;
    private RemoteWebDriver firefox;

    public void setUp() throws Exception {
        chrome = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), chromeCapabilities);
        firefox = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), firefoxCapabilities);

/*        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().setPosition(new Point(220, 10));
	    driver.manage().window().setSize(new Dimension(1000,650));
  */
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

