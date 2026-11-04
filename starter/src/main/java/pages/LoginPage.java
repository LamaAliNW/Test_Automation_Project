
package pages;

import config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("h3[data-test='error']");
    private static final By LOGO = By.className("login_logo");

    public LoginPage(WebDriver driver) {
        super(driver);
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
        click(LOGIN_BUTTON);
    }

    @Step("Read the error message")
    public String errorMessage() {
        return getText(ERROR).trim();
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
}
