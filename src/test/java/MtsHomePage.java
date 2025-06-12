import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class MtsHomePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Локаторы
    private final By paymentBlockTitle = By.xpath("//h2[contains(., 'Онлайн пополнение') and contains(., 'без комиссии')]");
    private final By paymentSystemsLogos = By.cssSelector("div.pay__partners ul li img");
    private final By detailsLink = By.linkText("Подробнее о сервисе");
    private final By continueButton = By.xpath("//button[contains(text(), 'Продолжить')]");
    private final By phoneInputField = By.id("connection-phone");
    private final By amountInputField = By.id("connection-sum");
    private final By serviceTypeDropdown = By.cssSelector("div.select__now");
    private final By serviceOptions = By.cssSelector("div.select__options p.select__option");
    private final By paymentIframe = By.cssSelector("iframe.bepaid-iframe");
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

    public void verifyPaymentBlockTitle() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentBlockTitle));
        Assert.assertTrue(title.isDisplayed(), "Payment block title is not displayed");
    }

    public void verifyPaymentSystemsLogos() {
        List<WebElement> logos = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(paymentSystemsLogos));
        Assert.assertEquals(logos.size(), 5, "Expected 5 payment system logos");
    }

    public void verifyDetailsLink() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(detailsLink));
        link.click();
        Assert.assertTrue(driver.getCurrentUrl().contains("/help/poryadok-oplaty"),
                "Details link doesn't lead to correct page");
        driver.navigate().back();
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
                verifyPlaceholder(phoneInputField, "Номер абонента");
                verifyPlaceholder(amountInputField, "Руб.");
                break;
            case "Домашний интернет":
                // Добавьте специфичные проверки для домашнего интернета
                break;
            case "Рассрочка":
                // Добавьте специфичные проверки для рассрочки
                break;
            case "Задолженность":
                // Добавьте специфичные проверки для задолженности
                break;
        }
    }

    private void verifyPlaceholder(By locator, String expectedPlaceholder) {
        WebElement field = driver.findElement(locator);
        String placeholder = field.getAttribute("placeholder");
        Assert.assertEquals(placeholder, expectedPlaceholder,
                "Incorrect placeholder for " + locator.toString());
    }

    public void fillPaymentForm(String phone, String amount) {
        WebElement phoneField = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInputField));
        phoneField.clear();
        phoneField.sendKeys(phone);

        WebElement amountField = driver.findElement(amountInputField);
        amountField.clear();
        amountField.sendKeys(amount);

        WebElement continueBtn = driver.findElement(continueButton);
        Assert.assertTrue(continueBtn.isEnabled(), "Continue button should be enabled after form filling");
        continueBtn.click();
    }

    public PaymentFrame switchToPaymentFrame() {
        WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentIframe));
        driver.switchTo().frame(iframe);
        return new PaymentFrame(driver);
    }
}