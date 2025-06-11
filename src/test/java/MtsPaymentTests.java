import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MtsPaymentTests extends BaseTest {

    MtsHomePage mtsHomePage;

    @BeforeClass
    public void setUpTest() {
        setUp();
        mtsHomePage = new MtsHomePage(driver);
        mtsHomePage.handleCookiePopup();
    }

    @Test(priority = 1)
    public void verifyPaymentBlockTitleTest() {
        mtsHomePage.verifyPaymentBlockTitle();
    }

    @Test(priority = 2)
    public void verifyPaymentSystemsLogosTest() {
        mtsHomePage.verifyPaymentSystemsLogos();
    }

    @Test(priority = 3)
    public void verifyDetailsLinkTest() {
        mtsHomePage.verifyDetailsLink();
    }

    @Test(priority = 4)
    public void testPaymentFormTest() {
        mtsHomePage.testPaymentForm();
    }

    @AfterClass
    public void tearDownTest() {
        tearDown();
    }
}