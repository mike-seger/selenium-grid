package com.net128.test.selenium;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.openqa.selenium.Dimension;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings({"WeakerAccess", "unused"})
public class Configuration {
    public String hubUrl;
    public String homePage;
    public String expectedTitle;
    public String screenshotDestination;
    public int initDriverDelayMs;
    public Browsers browsers;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Browser {
        public BrowserSize dimension;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Browsers {
        public Browser chrome;
        public Browser firefox;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BrowserSize extends Dimension {
        public BrowserSize() {
            super(0, 0);
        }
        public BrowserSize(int width, int height) {
            super(width, height);
        }
    }
}
