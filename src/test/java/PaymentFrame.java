import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

public class PaymentFrame {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By paymentAmount = By.xpath("//h1[contains(text(), 'BYN')]");
    private final By phoneNumber = By.xpath("//div[contains(text(), 'Номер:')]");
    private final By cardNumberField = By.xpath("//label[contains(text(), 'Номер карты')]/following-sibling::input");
    private final By expiryDateField = By.xpath("//label[contains(text(), 'Срок действия')]/following-sibling::input");
    private final By cvcField = By.xpath("//label[contains(text(), 'CVC')]/following-sibling::input");
    private final By cardNameField = By.xpath("//label[contains(text(), 'Имя и фамилия на карте')]/following-sibling::input");
    private final By paymentSystemsIcons = By.cssSelector("div.card-icons img");
    private final By payButton = By.xpath("//button[contains(text(), 'Оплатить')]");

    public PaymentFrame(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void verifyPaymentDetails(String expectedAmount, String expectedPhone) {
        // Проверка суммы
        WebElement amountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentAmount));
        String amountText = amountElement.getText();
        Assert.assertTrue(amountText.contains(expectedAmount),
                "Payment amount doesn't match. Expected: " + expectedAmount + ", Actual: " + amountText);

        // Проверка суммы на кнопке оплаты
        WebElement payBtn = driver.findElement(payButton);
        String buttonText = payBtn.getText();
        Assert.assertTrue(buttonText.contains(expectedAmount),
                "Pay button amount doesn't match. Expected: " + expectedAmount + ", Actual: " + buttonText);

        // Проверка номера телефона
        WebElement phoneElement = driver.findElement(phoneNumber);
        Assert.assertTrue(phoneElement.getText().contains(expectedPhone),
                "Phone number doesn't match. Expected: " + expectedPhone + ", Actual: " + phoneElement.getText());
    }

    public void verifyCardFieldsPlaceholders() {
        verifyFieldPlaceholder(cardNumberField, "Номер карты");
        verifyFieldPlaceholder(expiryDateField, "Срок действия");
        verifyFieldPlaceholder(cvcField, "CVC");
        verifyFieldPlaceholder(cardNameField, "Имя и фамилия на карте");
    }

    private void verifyFieldPlaceholder(By locator, String expectedPlaceholder) {
        WebElement field = driver.findElement(locator);
        String placeholder = field.getAttribute("placeholder");
        Assert.assertEquals(placeholder, expectedPlaceholder,
                "Incorrect placeholder for " + locator.toString());
    }

    public void verifyPaymentSystemsIcons() {
        List<WebElement> icons = driver.findElements(paymentSystemsIcons);
        Assert.assertTrue(icons.size() >= 2, "Expected at least 2 payment system icons");
    }
}