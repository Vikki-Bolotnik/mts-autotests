import io.qameta.allure.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

@Epic("Платежи МТС")
@Feature("Тестирование платежной формы")
public class MtsPaymentTests extends BaseTest {
    private MtsHomePage mtsHomePage;
    private static final String TEST_PHONE = "297777777";
    private static final String TEST_AMOUNT = "5";

    @BeforeClass
    @Step("Настройка тестового окружения")
    @Description("Инициализация драйвера и принятие cookies")
    public void setUpTest() {
        setUp();
        mtsHomePage = new MtsHomePage(driver);
        mtsHomePage.acceptCookies();
    }


    @Test(priority = 1)
    @Story("Проверка плейсхолдеров для различных сервисов")
    @Description("Проверка корректности отображения плейсхолдеров для всех сервисов")
    @Severity(SeverityLevel.NORMAL)
    public void verifyPlaceholdersForAllServices() {
        String[] services = {"Услуги связи", "Домашний интернет", "Рассрочка", "Задолженность"};

        for (String service : services) {
            mtsHomePage.verifyPlaceholdersForService(service);
        }
    }

    @Test(priority = 2)
    @Story("Тестирование формы мобильного платежа")
    @Description("Проверка формы оплаты для услуг связи")
    @Severity(SeverityLevel.CRITICAL)
    public void testMobilePaymentForm() {
        Allure.step("Шаг 1: Выбор сервиса 'Услуги связи'");
        mtsHomePage.selectConnectionService("Услуги связи");

        Allure.step("Шаг 2: Заполнение платежной формы");
        mtsHomePage.fillPaymentForm(TEST_PHONE, TEST_AMOUNT);

        Allure.step("Шаг 3: Переключение на платежный фрейм");
        PaymentFrame paymentFrame = mtsHomePage.switchToPaymentFrame();

        Allure.step("Шаг 4: Проверка деталей платежа");
        paymentFrame.verifyPaymentDetails(TEST_AMOUNT, TEST_PHONE);

        Allure.step("Шаг 5: Проверка полей карты");
        paymentFrame.verifyCardFieldsPlaceholders();

        Allure.step("Шаг 6: Проверка иконок платежных систем");
        paymentFrame.verifyPaymentSystemsIcons();

        driver.switchTo().defaultContent();
    }

    @AfterClass
    @Step("Завершение теста")
    @Description("Закрытие браузера и освобождение ресурсов")
    public void tearDownTest() {
        tearDown();
    }
}