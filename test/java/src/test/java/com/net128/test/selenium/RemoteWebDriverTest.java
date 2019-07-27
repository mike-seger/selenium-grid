package com.net128.test.selenium;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
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
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class RemoteWebDriverTest {
    private static RemoteWebDriver chrome;
    private static RemoteWebDriver firefox;
    private static HashMap<String, String> configuration;
    private static String screenshotDir;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        configuration=loadConfiguration();

        File dir=new File(configuration.get("screenshotDestination"));
        dir.mkdirs();
        screenshotDir=dir.getAbsolutePath();

        chrome = new RemoteWebDriver(new URL(configuration.get("hubBaseUrl")+"/wd/hub"), new ChromeOptions());
        firefox = new RemoteWebDriver(new URL(configuration.get("hubBaseUrl")+"/wd/hub"), new FirefoxOptions());
    }

    @Test
    public void testChrome() throws IOException {
        chrome.get(configuration.get("homePage"));
        assertEquals(configuration.get("expectedTitle"), chrome.getTitle());
        takeScreenshot(chrome, configuration.get("chrome"));
    }

    @Test
    public void testFirefox() throws IOException {
        firefox.get(configuration.get("homePage"));
        assertEquals(configuration.get("expectedTitle"), firefox.getTitle());
        takeScreenshot(firefox, configuration.get("firefox"));
    }

    @AfterClass
    public static void tearDownAfterClass() {
        chrome.quit();
        firefox.quit();
    }

    private static HashMap<String, String> loadConfiguration() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        String configName = "configuration.json";
        @SuppressWarnings("unchecked")
        HashMap<String, String> configuration = mapper.readValue(RemoteWebDriverTest.class
            .getResource("/"+configName), HashMap.class);
        if(new File(configName).exists())
            try (FileInputStream fis=new FileInputStream(configName);)
                { mapper.readerForUpdating(configuration).readValue(fis); }
        return configuration;
    }

    @SuppressWarnings("UnusedReturnValue")
    private File takeScreenshot(RemoteWebDriver driver, String namePrefix) throws IOException {
        Augmenter augmenter = new Augmenter();
        TakesScreenshot ts = (TakesScreenshot) augmenter.augment(driver);
        return moveScreenshot(ts.getScreenshotAs(OutputType.FILE), namePrefix);
    }

    private File moveScreenshot(File file, String namePrefix) throws IOException {
        String destName=namePrefix + "-" + getDateString() + ".png";
        File destFile=new File(screenshotDir, destName);
        Files.move(Paths.get(file.getAbsolutePath()), Paths.get(destFile.getAbsolutePath()));
        return destFile;
    }

    private String getDateString() {
        return DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC));
    }
}
