import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class MtsHomePage {

    final WebDriver driver;
    final WebDriverWait wait;

    private final By paymentBlockTitle = By.xpath("//h2[contains(., 'Онлайн пополнение') and contains(., 'без комиссии')]");
    private final By paymentSystemsLogos = By.cssSelector("div.pay__partners ul li img");
    private final By detailsLink = By.linkText("Подробнее о сервисе");
    private final By continueButton = By.xpath("//button[contains(text(), 'Продолжить')]");
    private static final String TEST_PHONE = "297777777";
    private static final String SUM = "5";
    private final By phoneInputField = By.id("connection-phone");
    private final By amountInputField = By.id("connection-sum");
    private static final String paymentHelpPageUrl = "https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/";
    private final By paymentIframe = By.cssSelector("iframe.bepaid-iframe");
    private final By serviceTypeDropdown = By.cssSelector("div.select__wrapper");
    //    private final By serviceOptions = By.cssSelector("ul.select__list");
    private final By serviceConnection = By.cssSelector("li.select__item.active");


    public MtsHomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void verifyPaymentBlockTitle() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentBlockTitle));
        assert title.isDisplayed();
    }

    public void verifyPaymentSystemsLogos() {
        List<WebElement> logos = driver.findElements(paymentSystemsLogos);
        Assert.assertEquals(logos.size(), 5);
    }

    public void verifyDetailsLink() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(detailsLink));
        link.click();
        assert driver.getCurrentUrl()
                .equals(paymentHelpPageUrl);
        driver.navigate().back();
    }

    public void testPaymentForm() {
        WebElement continueBtn = wait.until(
                ExpectedConditions.elementToBeClickable(continueButton));
        assert continueBtn.isEnabled() : "Кнопка 'Продолжить' неактивна до заполнения формы";
        driver.findElement(serviceTypeDropdown).click();
        WebElement serviceOption = wait.until(ExpectedConditions.
                visibilityOfElementLocated(serviceConnection));
        serviceOption.click();
        //driver.findElement(By.cssSelector("p.select__now:contains('Услуги связи')")).click();
        //driver.findElement(By.cssSelector("p.select__option:contains('Услуги связи')")).click();
        driver.findElement(phoneInputField).sendKeys(TEST_PHONE);
        driver.findElement(amountInputField).sendKeys(SUM);
        driver.findElement(continueButton).click();

        // Проверка загрузки платежного iframe
        try {
            WebElement iframe = wait.until(ExpectedConditions.visibilityOfElementLocated(paymentIframe));
            //     WebElement iframe2 = wait.until(ExpectedConditions.presenceOfElementLocated(paymentIframe));
            System.out.println("Платежная форма успешно загрузилась");
        } catch (TimeoutException e) {
            throw new AssertionError("Платежный iframe не загрузился после клика");
        }
    }

    void handleCookiePopup() {
        try {
            WebElement cookieAccept = driver.findElement(By.id("cookie-agree"));
            if (cookieAccept.isDisplayed()) {
                cookieAccept.click();
            }
        } catch (Exception e) {
            System.out.println("Куки-попап не найден");

        }
    }

    //    public void handleCookieBanner() {
//        try {
//            driver.switchTo().defaultContent();
//            driver.switchTo().frame(0);
//            driver.findElement(By.xpath("//button[contains(text(), 'Отклонить')]")).click();
//            driver.switchTo().defaultContent();
//        } catch (Exception e) {
//            System.out.println("Cookie баннер не найден или уже закрыт");
//        }
//    }
}

