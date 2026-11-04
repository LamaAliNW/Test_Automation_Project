
package pages;

import exceptions.ProductNotFoundException;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.PriceUtils;

import java.math.BigDecimal;
import java.util.List;

public class ProductsPage extends BasePage {

    // Locators
    private static final By TITLE =
            By.className("title");

    private static final By ITEM =
            By.className("inventory_item");

    private static final By ITEM_NAME =
            By.className("inventory_item_name");

    private static final By ITEM_PRICE =
            By.className("inventory_item_price");

    private static final By SORT =
            By.className("product_sort_container");

    private static final By CART_LINK =
            By.className("shopping_cart_link");


    // Constructor
    public ProductsPage(WebDriver driver) {
        super(driver);
    }


    // Verify products page is loaded
    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains("inventory.html")
            );

            return isVisible(TITLE);

        } catch (TimeoutException e) {
            return false;
        }
    }


    // Read page heading
    public String heading() {
        return getText(TITLE).trim();
    }


    // Count products
    public int productCount() {
        return driver.findElements(ITEM).size();
    }


    // Read all product names
    public List<String> productNames() {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(ITEM)
        );

        return driver.findElements(ITEM_NAME)
                .stream()
                .map(WebElement::getText)
                .toList();
    }


    // Read product prices as double
    // Kept for compatibility with existing tests
    public List<Double> productPrices() {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(ITEM)
        );

        return driver.findElements(ITEM_PRICE)
                .stream()
                .map(element ->
                        Double.parseDouble(
                                element.getText().replace("$", "")
                        )
                )
                .toList();
    }


    // Read product prices using BigDecimal
    public List<BigDecimal> productPricesAsBigDecimal() {
        wait.until(
                ExpectedConditions.visibilityOfElementLocated(ITEM)
        );

        return driver.findElements(ITEM_PRICE)
                .stream()
                .map(element ->
                        PriceUtils.parse(element.getText())
                )
                .toList();
    }


    // Check if a product exists
    public boolean hasProduct(String productName) {
        return productNames().contains(productName);
    }


    // Find product card or throw custom exception
    private WebElement findProduct(String productName) {

        for (WebElement product : driver.findElements(ITEM)) {

            String name = product.findElement(ITEM_NAME)
                    .getText()
                    .trim();

            if (name.equals(productName)) {
                return product;
            }
        }

        throw new ProductNotFoundException(
                productName,
                productNames()
        );
    }


    // Check product button state
    public boolean isProductAdded(String productName) {

        WebElement product = findProduct(productName);

        return product.findElement(By.tagName("button"))
                .getText()
                .trim()
                .equalsIgnoreCase("Remove");
    }


    // Add a specific product to the cart
    @Step("Add {0} to the cart")
    public void addToCart(String productName) {

        findProduct(productName);

        if (!isProductAdded(productName)) {
            click(buttonForProduct(productName));
        }

        // Wait until the button changes to Remove
        wait.until(driver ->
                isProductAdded(productName)
        );
    }


    // Remove a specific product from the cart
    @Step("Remove {0} from the cart")
    public void removeFromCart(String productName) {

        findProduct(productName);

        if (isProductAdded(productName)) {
            click(buttonForProduct(productName));
        }
    }


    // Sort products
    @Step("Sort products by {0}")
    public ProductsPage sortBy(String optionValue) {

        WebElement dropdown = wait.until(
                ExpectedConditions.visibilityOfElementLocated(SORT)
        );

        new Select(dropdown).selectByValue(optionValue);

        return this;
    }


    // Open product details
    @Step("Open details for {0}")
    public ProductDetailsPage openProductDetails(String productName) {

        WebElement product = findProduct(productName);

        product.findElement(ITEM_NAME).click();

        return new ProductDetailsPage(driver);
    }


    // Open shopping cart
    @Step("Open shopping cart")
    public CartPage openCart() {

        click(CART_LINK);

        // Wait until navigation to the cart is complete
        wait.until(
                ExpectedConditions.urlContains("cart.html")
        );

        return new CartPage(driver);
    }


    // Get current URL
    public String currentUrl() {
        return driver.getCurrentUrl();
    }


    // Locate the button for a specific product
    private By buttonForProduct(String productName) {

        return By.xpath(
                "//div[contains(@class,'inventory_item')]"
                        + "[.//div[contains(@class,'inventory_item_name')]"
                        + "[normalize-space()='" + productName + "']]"
                        + "//button"
        );
    }
}
