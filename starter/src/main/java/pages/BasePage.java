
package pages;

import config.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    // Shared locators
    private static final By HEADER =
            By.className("title");

    private static final By CART_BADGE =
            By.className("shopping_cart_badge");

    private static final By MENU_BUTTON =
            By.id("react-burger-menu-btn");

    private static final By LOGOUT_BUTTON =
            By.id("logout_sidebar_link");

    // Constructor
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.timeout());
    }

    // Click an element
    protected void click(By locator) {
        wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        ).click();
    }

    // Enter text
    protected void type(By locator, String text) {
        WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );

        element.clear();
        element.sendKeys(text);
    }

    // Read text
    protected String getText(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        ).getText();
    }

    // Check element visibility
    protected boolean isVisible(By locator) {
        try {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            ).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Read page heading
    public String header() {
        return getText(HEADER).trim();
    }

    // Read cart badge
    public int cartCount() {
        List<WebElement> badges =
                driver.findElements(CART_BADGE);

        if (badges.isEmpty()) {
            return 0;
        }

        return Integer.parseInt(
                badges.get(0).getText()
        );
    }

    // Log out and return to LoginPage
    public LoginPage logout() {
        click(MENU_BUTTON);
        click(LOGOUT_BUTTON);

        return new LoginPage(driver);
    }
}
