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
    private final By paymentAmount = By.xpath("//div[contains(@class, 'pay-description__cost')]/span");
    private final By phoneNumber = By.xpath("//div[contains(@class, 'pay-description__text");
    private final By cardNumberField = By.xpath("//label[text()='Номер карты']");
    private final By expiryDateField = By.xpath("//label[text()='Срок действия']");
    private final By cvcField = By.xpath("//label[text()='CVC']");
    private final By cardNameField = By.xpath("//label[text()='Имя и фамилия на карте']");
    private final By paymentSystemsIcons = By.xpath("//div[contains(@class, 'cards-brands cards-brands__container')]");
    private final By payButton = By.xpath("//button[contains(text(),'Оплатить')]");

    public PaymentFrame(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void verifyPaymentDetails(String expectedAmount, String expectedPhone) {

        WebElement amountElement = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentAmount));
        String amountText = amountElement.getText();
        Assert.assertTrue(amountText.contains(expectedAmount),
                "Payment amount doesn't match. Expected: " + expectedAmount + ", Actual: " + amountText);

        WebElement payBtn = driver.findElement(payButton);
        String buttonText = payBtn.getText().trim();
        Assert.assertTrue(buttonText.contains(expectedAmount),
                "Pay button amount doesn't match. Expected: " + expectedAmount + ", Actual: " + buttonText);

        WebElement phoneElement = driver.findElement(phoneNumber);
        Assert.assertTrue(phoneElement.getText().contains(expectedPhone),
                "Phone number doesn't match. Expected: " + expectedPhone + ", Actual: " + phoneElement.getText());
    }

    public void verifyCardFieldsPlaceholders() {
        verifyFieldText(cardNumberField, "Номер карты");
        verifyFieldText(expiryDateField, "Срок действия");
        verifyFieldText(cvcField, "CVC");
        verifyFieldText(cardNameField, "Имя и фамилия на карте");
    }

//    private void verifyFieldPlaceholder(By locator, String expectedPlaceholder) {
//        WebElement field = driver.findElement(locator);
//        String placeholder = field.getText().trim();
//        Assert.assertEquals(placeholder, expectedPlaceholder,
//                "Incorrect placeholder for " + locator.toString());
//    }

    private void verifyFieldText(By locator, String expectedText) {
        WebElement field = driver.findElement(locator);
        String actualText = field.getText().trim();
        Assert.assertEquals(actualText, expectedText,
                "Incorrect text for element located by " + locator.toString());
    }
    public void verifyPaymentSystemsIcons() {
        List<WebElement> icons = driver.findElements(paymentSystemsIcons);
        Assert.assertEquals(icons.size() , 4,"Expected 5 payment system icons");
    }

}