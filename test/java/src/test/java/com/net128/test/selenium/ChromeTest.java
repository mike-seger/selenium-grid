package com.net128.test.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class ChromeTest {
	@Test
	public void test() throws Exception {
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
		driver.manage().timeouts().getScriptTimeout();
		driver.get("http://web/");
//		driver.get("https://google.com/");
		driver.getWindowHandle();
		driver.getCurrentUrl();
		driver.quit();
	}
}
