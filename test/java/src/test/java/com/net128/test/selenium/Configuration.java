package com.net128.test.selenium;

import org.openqa.selenium.Dimension;

@SuppressWarnings({"WeakerAccess ","unused"})
class Configuration {
    public String hubUrl;
    public String homePage;
    public String expectedTitle;
    public String screenshotDestination;
    public int initDriverDelayMs;
    public Browsers browsers;

    static class Browsers {
        public Browser chrome;
        public Browser firefox;
        static class Browser {
            public Size dimension;
            static class Size extends Dimension {
                public Size() {
                    super(0, 0);
                }
                public Size(int width, int height) {
                    super(width, height);
                }
            }
        }
    }
}
