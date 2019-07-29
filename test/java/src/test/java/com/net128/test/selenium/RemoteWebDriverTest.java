package com.net128.test.selenium;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.skjolber.jackson.jsh.AnsiSyntaxHighlight;
import com.github.skjolber.jackson.jsh.DefaultSyntaxHighlighter;
import com.github.skjolber.jackson.jsh.SyntaxHighlighter;
import com.github.skjolber.jackson.jsh.SyntaxHighlightingJsonGenerator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings({"unused", "ResultOfMethodCallIgnored", })
public class RemoteWebDriverTest {
    private static final Logger logger = LoggerFactory.getLogger(RemoteWebDriverTest.class.getSimpleName());
    private static RemoteWebDriver chrome;
    private static RemoteWebDriver firefox;
    private static Configuration configuration;
    private static File screenshotDir;

    @BeforeAll
    static void setup() throws Exception {
        configuration=loadConfiguration();
        screenshotDir=new File(configuration.screenshotDestination);
        screenshotDir.mkdirs();
        logger.info("Setting up drivers");
        chrome = new RemoteWebDriver(new URL(configuration.hubUrl), new ChromeOptions());
        firefox = new RemoteWebDriver(new URL(configuration.hubUrl), new FirefoxOptions());
        chrome.manage().window().setSize(configuration.browsers.chrome.dimension);
        firefox.manage().window().setSize(configuration.browsers.firefox.dimension);
        logger.info("Done setting up drivers");
    }

    @Test
    void testChrome() throws IOException {
        testDriver(chrome, "chrome");
    }

    @Test
    void testFirefox() throws IOException {
        testDriver(firefox, "firefox");
    }

    @AfterAll
    static void teardown() {
        logger.info("Quitting drivers");
        chrome.quit();
        firefox.quit();
        logger.info("Done quitting drivers");
    }

    private void testDriver(RemoteWebDriver driver, String screenshotPrefix) throws IOException {
        driver.get(configuration.homePage);
        assertEquals(configuration.expectedTitle, driver.getTitle());
        takeScreenshot(driver, screenshotPrefix);
    }

    private String getDateString() {
        SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd-HHmmss");
        f.setTimeZone(TimeZone.getTimeZone("UTC"));
        return f.format(new Date());
    }

    @SuppressWarnings("UnusedReturnValue")
    private File takeScreenshot(RemoteWebDriver driver, String namePrefix) throws IOException {
        Augmenter augmenter = new Augmenter();
        TakesScreenshot ts = (TakesScreenshot) augmenter.augment(driver);
        File destFile=new File(screenshotDir,namePrefix + "-" + getDateString() + ".png");
        Files.write(destFile.toPath(), ts.getScreenshotAs(OutputType.BYTES));
        return destFile;
    }

    private static Configuration loadConfiguration() throws IOException {
        ObjectMapper mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String configName = "configuration.json";
        Configuration configuration = mapper.readValue(RemoteWebDriverTest.class
                .getResource("/"+configName), Configuration.class);
        if(new File(configName).exists())
            try (FileInputStream fis=new FileInputStream(configName))
            { mapper.readerForUpdating(configuration).readValue(fis); }
        logger.info("Active Configuration:\n{}", colorizedJson(configuration));
        return configuration;
    }

    private static String colorizedJson(Object o) {
        ObjectMapper om=new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            JsonFactory jsonFactory = new JsonFactory();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            JsonGenerator delegate = jsonFactory.createGenerator(baos, JsonEncoding.UTF8);
            SyntaxHighlighter highlighter = DefaultSyntaxHighlighter
                .newBuilder()
                .withField(AnsiSyntaxHighlight.BLUE)
                .withString(AnsiSyntaxHighlight.GREEN)
                .withNumber(AnsiSyntaxHighlight.CYAN)
                .withCurlyBrackets(AnsiSyntaxHighlight.CYAN)
                .withComma(AnsiSyntaxHighlight.WHITE)
                .withColon(AnsiSyntaxHighlight.WHITE)
                .build();
            try (JsonGenerator jsonGenerator = new SyntaxHighlightingJsonGenerator(delegate, highlighter)) {
                jsonGenerator.setCodec(om);
                jsonGenerator.writeObject(o);
                baos.write(AnsiSyntaxHighlight.RESET.getBytes());
                return baos.toString();
            } catch(Exception e) {
                return om.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert to JSON", e);
        }
    }

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
