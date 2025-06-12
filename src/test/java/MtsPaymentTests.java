import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

public class MtsPaymentTests extends BaseTest {
    private MtsHomePage mtsHomePage;
    private static final String TEST_PHONE = "297777777";
    private static final String TEST_AMOUNT = "5";

    @BeforeClass
    public void setUpTest() {
        setUp();
        mtsHomePage = new MtsHomePage(driver);
        mtsHomePage.acceptCookies();
    }


    @Test(priority = 1)
    public void verifyPlaceholdersForAllServices() {
        String[] services = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};

        for (String service : services) {
            mtsHomePage.verifyPlaceholdersForService(service);
        }
    }

    @Test(priority = 2)
    public void testMobilePaymentForm() {
        // Выбираем "Услуги связи"
        mtsHomePage.selectServiceType("Услуги связи");

        // Заполняем форму
        mtsHomePage.fillPaymentForm(TEST_PHONE, TEST_AMOUNT);

        // Переключаемся на платежный фрейм и проверяем данные
        PaymentFrame paymentFrame = mtsHomePage.switchToPaymentFrame();
        paymentFrame.verifyPaymentDetails(TEST_AMOUNT, TEST_PHONE);
        paymentFrame.verifyCardFieldsPlaceholders();
        paymentFrame.verifyPaymentSystemsIcons();

        // Возвращаемся к основному контенту
        driver.switchTo().defaultContent();
    }

    @AfterClass
    public void tearDownTest() {
        tearDown();
    }
}