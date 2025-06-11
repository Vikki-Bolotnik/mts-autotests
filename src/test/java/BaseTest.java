import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

//    protected void setUp() {
//        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
//        WebDriver driver = new ChromeDriver();
//       driver.get("https://www.mts.by");
////        WebDriverManager.chromedriver().driverVersion( "114.0.5735.90").setup();
////        WebDriver driver = new ChromeDriver();
////        driver.get("https://www.mts.by");
//        driver.manage().window().maximize();
//        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//    }

    protected void setUp() {
        WebDriverManager.chromedriver().setup();
        this.driver = new ChromeDriver();
        driver.get("https://www.mts.by");
        driver.manage().window().maximize();

        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    protected void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}