import junit.framework.TestCase; 
import org.openqa.selenium.*;
import org.openqa.selenium.remote.*;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WebDriverTest extends TestCase {
    private WebDriver driver;
    
    public void setUp() throws Exception {
        DesiredCapabilities abilities = DesiredCapabilities.firefox();
        capabillities.setCapability("version", "16");
        capabillities.setCapability("platform", Platform.WINDOWS);
        capabillities.setCapability("name", "Testing Selenium-2 Remote WebDriver");

        driver = new RemoteWebDriver( new URL("http://key:secret@hub.testingbot.com:4444/wd/hub"), abilities);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().setPosition(new Point(220, 10));
	driver.manage().window().setSize(new Dimension(1000,650));
    }

    public void testSimple() throws Exception {
        this.driver.get("http://www.google.com");
        assertEquals("Google", this.driver.getTitle());
    }

    public void tearDown() throws Exception {
        this.driver.quit();
    }
}

