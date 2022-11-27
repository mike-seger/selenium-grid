package com.net128.test.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GridTest {
	private WebDriver driver;
	@BeforeAll
	public void setup() throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
	}

	@AfterAll
	public void closeBrowser() {
		driver.quit();
	}

	@Test
	public void test() {
		driver.get("http://web/");
		assertEquals("UA Info", driver.getTitle());
	}
}
