import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class MtsHomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By continueButton = By.xpath("//button[contains(text(), 'Продолжить')]");

    private final By connectionPhoneInputField = By.id("connection-phone");
    private final By connectionAmountInputField = By.id("connection-sum");
    private final By connectionEmailInputField = By.id("connection-email");

    private final By internetInputField = By.id("internet-phone");
    private final By internetAmountInputField = By.id("internet-sum");
    private final By internetEmailInputField = By.id("internet-email");

    private final By installmentInputField = By.id("score-instalment");
    private final By installmentAmountInputField = By.id("instalment-sum");
    private final By installmentEmailInputField = By.id("instalment-email");

    private final By debtInputField = By.id("score-arrears");
    private final By debtAmountInputField = By.id("arrears-sum");
    private final By debtEmailInputField = By.id("arrears-email");

    private final By serviceTypeDropdown = By.cssSelector("div.select__wrapper");
    private final By serviceOptions = By.cssSelector("ul.select__list");
    private final By serviceConnection = By.cssSelector("li.select__item.active");

    private final By paymentIframe = By.cssSelector("iframe.bepaid-iframe[src*='checkout.bepaid.by']");
    private final By paymentCostSection = By.xpath("//div[contains(@class, 'pay-description__cost')]");
    private final By cookieAcceptButton = By.id("cookie-agree");

    public MtsHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void acceptCookies() {
        try {
            WebElement cookieAccept = wait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton));
            cookieAccept.click();
        } catch (Exception e) {
            System.out.println("Cookie popup not found or already closed");
        }
    }

    public void selectServiceType(String serviceName) {
        driver.findElement(serviceTypeDropdown).click();
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(serviceOptions));

        for (WebElement option : options) {
            if (option.getText().contains(serviceName)) {
                option.click();
                return;
            }
        }
        throw new NoSuchElementException("Service option not found: " + serviceName);
    }

    public void verifyPlaceholdersForService(String serviceName) {
        selectServiceType(serviceName);

        switch (serviceName) {
            case "Услуги связи":
                verifyPlaceholder(connectionPhoneInputField, "Номер телефона");
                verifyPlaceholder(connectionAmountInputField, "Сумма");
                verifyPlaceholder(connectionEmailInputField, "E-mail для отправки чека");
                break;
            case "Домашний интернет":
                verifyPlaceholder(internetInputField, "Номер абонента");
                verifyPlaceholder(internetAmountInputField, "Сумма");
                verifyPlaceholder(internetEmailInputField, "E-mail для отправки чека");
                break;
            case "Рассрочка":
                verifyPlaceholder(installmentInputField, "Номер счета на 44");
                verifyPlaceholder(installmentAmountInputField, "Сумма");
                verifyPlaceholder(installmentEmailInputField, "E-mail для отправки чека");
                break;
            case "Задолженность":
                verifyPlaceholder(debtInputField, "Номер счета на 2073");
                verifyPlaceholder(debtAmountInputField, "Сумма");
                verifyPlaceholder(debtEmailInputField, "E-mail для отправки чека");
                break;
        }
    }

    private void verifyPlaceholder(By locator, String expectedPlaceholder) {
        WebElement field = driver.findElement(locator);
        String placeholder = field.getAttribute("placeholder");
        Assert.assertEquals(placeholder, expectedPlaceholder,
                "Incorrect placeholder for " + locator.toString());
    }

    @Step("Выбор сервиса: {serviceName}")
    public void selectConnectionService(String serviceName) {
        driver.findElement(serviceTypeDropdown).click();

        if (serviceName != null && !serviceName.isEmpty()) {
            WebElement serviceOption = wait.until(ExpectedConditions.
                    visibilityOfElementLocated(serviceConnection));
            serviceOption.click();
        } else {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
    }

    @Step("Заполнение формы: телефон {phone}, сумма {amount}")
    public void fillPaymentForm(String phone, String amount) {
        WebElement phoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(connectionPhoneInputField));
        phoneField.sendKeys(phone);

        WebElement amountField = driver.findElement(connectionAmountInputField);
        amountField.sendKeys(amount);

        WebElement continueBtn = driver.findElement(continueButton);
        Assert.assertTrue(continueBtn.isEnabled(), "Continue button should be enabled after form filling");
        continueBtn.click();
    }

    @Step("Переключение на платежный фрейм")
    public PaymentFrame switchToPaymentFrame() {
        try {
            WebElement iframe = wait.until(ExpectedConditions.presenceOfElementLocated(paymentIframe));
            driver.switchTo().frame(iframe);
            wait.until(ExpectedConditions.visibilityOfElementLocated(paymentCostSection));
            return new PaymentFrame(driver);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось переключиться на платежный фрейм", e);
        }
    }
//    public PaymentFrame switchToPaymentFrame2() {
//        WebElement iframe = (WebElement) ((JavascriptExecutor) driver).executeScript(
//                "return document.querySelector('iframe.bepaid-iframe');"
//        );
//        driver.switchTo().frame(iframe);
//
////        WebElement container = driver.findElement(By.cssSelector(".bepaid-app__container"));
////        WebElement iframe = container.findElement(By.tagName("iframe"));
////        driver.switchTo().frame(iframe);
//
////        WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentIframe));
////        driver.switchTo().frame(0);
//
//        return new PaymentFrame(driver);
//    }

}