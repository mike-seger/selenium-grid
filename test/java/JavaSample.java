import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class JavaSample {

  public static final String KEY = "KEY";
  public static final String SECRET = "SECRET";
  public static final String URL = "http://" + KEY + ":" + SECRET + "@hub.testingbot.com/wd/hub";

  public static void main(String[] args) throws Exception {

	DesiredCapabilities caps = new DesiredCapabilities();
	caps.setCapability("browserName", "IE");
	caps.setCapability("version", "11");
	caps.setCapability("platform", "WIN10");
	caps.setCapability("name", "My First Test");

	WebDriver driver = new RemoteWebDriver(new URL(URL), caps);
	driver.get("http://www.google.com/ncr");
	WebElement element = driver.findElement(By.name("q"));

	element.sendKeys("TestingBot");
	element.submit();

	System.out.println(driver.getTitle());
	driver.quit();
  }
}
