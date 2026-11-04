
package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.PriceUtils;

import java.math.BigDecimal;

public class ProductDetailsPage extends BasePage {

    // Locators
    private static final By PRODUCT_NAME =
            By.className("inventory_details_name");

    private static final By PRODUCT_DESCRIPTION =
            By.className("inventory_details_desc");

    private static final By PRODUCT_PRICE =
            By.className("inventory_details_price");

    private static final By ADD_BUTTON =
            By.cssSelector("button.btn_inventory");

    private static final By BACK_BUTTON =
            By.id("back-to-products");

    private static final By CART_LINK =
            By.className("shopping_cart_link");


    // Constructor
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }


    // Verify details page is loaded
    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains(
                            "inventory-item.html"
                    )
            );

            return isVisible(PRODUCT_NAME);

        } catch (TimeoutException e) {
            return false;
        }
    }


    // Read product name
    public String productName() {
        return getText(PRODUCT_NAME).trim();
    }


    // Read product description
    public String productDescription() {
        return getText(PRODUCT_DESCRIPTION).trim();
    }


    // Read product price
    public BigDecimal productPrice() {
        return PriceUtils.parse(
                getText(PRODUCT_PRICE)
        );
    }


    // Check if product is added
    public boolean isProductAdded() {
        return getText(ADD_BUTTON)
                .trim()
                .equalsIgnoreCase("Remove");
    }


    // Add product to cart
    @Step("Add product from details page")
    public ProductDetailsPage addToCart() {

        if (!isProductAdded()) {
            click(ADD_BUTTON);
        }

        return this;
    }


    // Remove product from cart
    @Step("Remove product from details page")
    public ProductDetailsPage removeFromCart() {

        if (isProductAdded()) {
            click(ADD_BUTTON);
        }

        return this;
    }


    // Return to products page
    @Step("Back to products")
    public ProductsPage backToProducts() {

        click(BACK_BUTTON);

        return new ProductsPage(driver);
    }


    // Open cart
    @Step("Open cart from product details")
    public CartPage openCart() {

        click(CART_LINK);

        return new CartPage(driver);
    }
}
