package com.net128.test.selenium;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class RemoteWebDriverTest extends TestCase {
    private ChromeOptions chromeOptions = new ChromeOptions();
    private FirefoxOptions firefoxOptions = new FirefoxOptions();
    private RemoteWebDriver chrome;
    private RemoteWebDriver firefox;
    private HashMap<String, String> configuration;
    private String screenshotDir;

    @SuppressWarnings("unused")
    public void setUp() throws Exception {
        configuration=loadConfiguration();

        File dir=new File(configuration.get("screenshotDestination"));

        dir.mkdirs();
        screenshotDir=dir.getAbsolutePath();

        chrome = new RemoteWebDriver(new URL(configuration.get("hubBaseUrl")+"/wd/hub"), chromeOptions);
        firefox = new RemoteWebDriver(new URL(configuration.get("hubBaseUrl")+"/wd/hub"), firefoxOptions);
    }

    public void testChrome() throws IOException {
        chrome.get(configuration.get("homePage"));
        assertEquals(configuration.get("expectedTitle"), chrome.getTitle());
        takeScreenshot(chrome, configuration.get("chrome"));
    }

    public void testFirefox() throws IOException {
        firefox.get(configuration.get("homePage"));
        assertEquals(configuration.get("expectedTitle"), firefox.getTitle());
        takeScreenshot(firefox, configuration.get("firefox"));
    }

    public void tearDown() {
        chrome.quit();
        firefox.quit();
    }

    private HashMap<String, String> loadConfiguration() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        String configName = "configuration.json";
        @SuppressWarnings("unchecked")
        HashMap<String, String> configuration = mapper.readValue(getClass()
            .getResource("/"+configName), HashMap.class);
        try (FileInputStream fis=new FileInputStream(configName))
        { mapper.readerForUpdating(configuration).readValue(fis); }
        catch(FileNotFoundException e) { /**/ }
        catch(Exception e) { e.printStackTrace(); }
        return configuration;
    }

    @SuppressWarnings("UnusedReturnValue")
    private File takeScreenshot(RemoteWebDriver driver, String namePrefix) throws IOException {
        Augmenter augmenter = new Augmenter();
        TakesScreenshot ts = (TakesScreenshot) augmenter.augment(driver);
        File file = ts.getScreenshotAs(OutputType.FILE);
        String destName=namePrefix + "-" +
            Instant.now().toString()
                .replaceAll("[.][0-9]]", "")
                .replaceAll("[:.-]", "")+".png";
        File destFile=new File(screenshotDir, destName);
        Files.move(Paths.get(file.getAbsolutePath()), Paths.get(destFile.getAbsolutePath()));
        if(!destFile.exists()) {
            throw new RuntimeException("Could move screenshot from: "+file+" to "+destFile);
        }
        return destFile;
    }
}

