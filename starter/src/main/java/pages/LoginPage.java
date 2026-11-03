package pages;

import config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {


    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("h3[data-test='error']");
    private static final By LOGO = By.className("login_logo");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.timeout());
    }

    @Step("Open the login page")
    public LoginPage open() {
        driver.get(Config.baseUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME));
        return this;
    }

    @Step("Log in as {0}")
    public ProductsPage loginAs(String user, String password) {
        submit(user, password);
        return new ProductsPage(driver);
    }

    @Step("Try to log in as {0}")
    public LoginPage loginExpectingFailure(String user, String password) {
        submit(user, password);
        return this;
    }

    private void submit(String user, String password) {
        type(USERNAME, user);
        type(PASSWORD, password);
        wait.until(ExpectedConditions.elementToBeClickable(LOGIN_BUTTON)).click();
    }

    private void type(By locator, String text) {

        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        field.clear();
        field.sendKeys(text);
    }

    @Step("Read the error message")
    public String errorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(ERROR)).getText().trim();
    }

    public boolean isLoaded() {
        return isVisible(LOGO) && isVisible(LOGIN_BUTTON);
    }

    public boolean isUsernameVisible() {
        return isVisible(USERNAME);
    }

    public boolean isPasswordVisible() {
        return isVisible(PASSWORD);
    }

    public boolean isLoginButtonVisible() {
        return isVisible(LOGIN_BUTTON);
    }

    private boolean isVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
