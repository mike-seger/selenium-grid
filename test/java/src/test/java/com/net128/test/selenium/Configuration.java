package com.net128.test.selenium;

import org.openqa.selenium.Dimension;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Configuration {
    public String hubUrl;
    public String homePage;
    public String expectedTitle;
    public String screenshotDestination;
    public int initDriverDelayMs;
    public Browsers browsers;

    public static class Browser {
        public BrowserSize dimension;
    }

    public static class Browsers {
        public Browser chrome;
        public Browser firefox;
    }

    public static class BrowserSize extends Dimension {
        public BrowserSize() {
            super(0, 0);
        }
        public BrowserSize(int width, int height) {
            super(width, height);
        }
    }
}
