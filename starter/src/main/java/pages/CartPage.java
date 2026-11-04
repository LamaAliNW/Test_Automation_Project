
package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.PriceUtils;

import java.math.BigDecimal;
import java.util.List;

public class CartPage extends BasePage {

    // Locators
    private static final By TITLE =
            By.className("title");

    private static final By CART_ITEM =
            By.className("cart_item");

    private static final By ITEM_NAME =
            By.className("inventory_item_name");

    private static final By ITEM_PRICE =
            By.className("inventory_item_price");

    private static final By ITEM_QUANTITY =
            By.className("cart_quantity");

    private static final By CONTINUE_SHOPPING =
            By.id("continue-shopping");

    private static final By CHECKOUT_BUTTON =
            By.id("checkout");


    // Constructor
    public CartPage(WebDriver driver) {
        super(driver);
    }


    // Verify cart page is loaded
    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains("cart.html")
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


    // Count cart items
    public int itemCount() {
        return driver.findElements(CART_ITEM).size();
    }


    // Read product names
    public List<String> productNames() {
        return driver.findElements(ITEM_NAME)
                .stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();
    }


    // Read product prices
    public List<BigDecimal> productPrices() {
        return driver.findElements(ITEM_PRICE)
                .stream()
                .map(element ->
                        PriceUtils.parse(element.getText())
                )
                .toList();
    }


    // Read item quantities
    public List<Integer> quantities() {
        return driver.findElements(ITEM_QUANTITY)
                .stream()
                .map(element ->
                        Integer.parseInt(
                                element.getText().trim()
                        )
                )
                .toList();
    }


    // Check if product exists in cart
    public boolean containsProduct(String productName) {
        return productNames().contains(productName);
    }


    // Remove a specific product
    @Step("Remove {0} from cart")
    public CartPage removeProduct(String productName) {

        WebElement item = findCartItem(productName);

        item.findElement(
                By.cssSelector("button.cart_button")
        ).click();

        return this;
    }


    // Find cart item by product name
    private WebElement findCartItem(String productName) {

        for (WebElement item : driver.findElements(CART_ITEM)) {

            String name = item.findElement(ITEM_NAME)
                    .getText()
                    .trim();

            if (name.equals(productName)) {
                return item;
            }
        }

        throw new exceptions.ProductNotFoundException(
                productName,
                productNames()
        );
    }


    // Continue shopping
    @Step("Continue shopping")
    public ProductsPage continueShopping() {

        click(CONTINUE_SHOPPING);

        return new ProductsPage(driver);
    }


    // Proceed to checkout
    @Step("Proceed to checkout")
    public CheckoutInformationPage checkout() {

        click(CHECKOUT_BUTTON);

        return new CheckoutInformationPage(driver);
    }
}
