package com.net128.test.selenium;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class RemoteWebDriverTest {
    private static RemoteWebDriver chrome;
    private static RemoteWebDriver firefox;
    private static HashMap<String, String> configuration;
    private static File screenshotDir;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        configuration=loadConfiguration();
        screenshotDir=new File(configuration.get("screenshotDestination"));
        screenshotDir.mkdirs();
        chrome = new RemoteWebDriver(new URL(configuration.get("hubUrl")), new ChromeOptions());
        firefox = new RemoteWebDriver(new URL(configuration.get("hubUrl")), new FirefoxOptions());
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

    private String getDateString() {
        return DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss")
                .format(ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.UTC));
    }

    @SuppressWarnings("UnusedReturnValue")
    private File takeScreenshot(RemoteWebDriver driver, String namePrefix) throws IOException {
        Augmenter augmenter = new Augmenter();
        TakesScreenshot ts = (TakesScreenshot) augmenter.augment(driver);
        File destFile=new File(screenshotDir,namePrefix + "-" + getDateString() + ".png");
        Files.write(destFile.toPath(), ts.getScreenshotAs(OutputType.BYTES));
        return destFile;
    }

    private static HashMap<String, String> loadConfiguration() throws IOException {
        ObjectMapper mapper=new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String configName = "configuration.json";
        HashMap<String, String> configuration = mapper.readValue(RemoteWebDriverTest.class
            .getResource("/"+configName), new TypeReference<HashMap<String,String>>() {});
        if(new File(configName).exists())
            try (FileInputStream fis=new FileInputStream(configName))
            { mapper.readerForUpdating(configuration).readValue(fis); }
        System.out.println("Configuration: "+mapper.writeValueAsString(configuration));
        return configuration;
    }
}
