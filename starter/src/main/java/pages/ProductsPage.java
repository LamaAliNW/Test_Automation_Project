package pages;

import config.Config;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ProductsPage {

    private static final By TITLE = By.className("title");
    private static final By ITEM = By.className("inventory_item");
    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ITEM_PRICE = By.className("inventory_item_price");
    private static final By SORT = By.className("product_sort_container");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT = By.id("logout_sidebar_link");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.timeout());
    }

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("inventory.html"));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String heading() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE)).getText().trim();
    }

    public int productCount() {
        return driver.findElements(ITEM).size();
    }

    public List<String> productNames() {
        return driver.findElements(ITEM_NAME).stream().map(WebElement::getText).toList();
    }

    public List<Double> productPrices() {
        return driver.findElements(ITEM_PRICE).stream()
                .map(element -> Double.parseDouble(element.getText().replace("$", "")))
                .toList();
    }

    @Step("Add {0} to the cart")
    public void addToCart(String productName) {
        wait.until(ExpectedConditions.elementToBeClickable(addButtonFor(productName))).click();
    }

    @Step("Sort the products by {0}")
    public ProductsPage sortBy(String optionValue) {
        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(SORT));
        new Select(dropdown).selectByValue(optionValue);
        return this;
    }

    public int cartCount() {
        List<WebElement> badge = driver.findElements(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText());
    }

    @Step("Log out")
    public LoginPage logout() {
        wait.until(ExpectedConditions.elementToBeClickable(MENU_BUTTON)).click();
        wait.until(ExpectedConditions.elementToBeClickable(LOGOUT)).click();
        return new LoginPage(driver);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    private By addButtonFor(String productName) {
        return By.xpath("//div[@class='inventory_item']"
                + "[.//div[contains(@class,'inventory_item_name')][normalize-space()='" + productName + "']]"
                + "//button");
    }
}
