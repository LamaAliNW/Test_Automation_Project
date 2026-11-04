
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

public class CheckoutOverviewPage extends BasePage {

    // Locators
    private static final By TITLE =
            By.className("title");

    private static final By CART_ITEM =
            By.className("cart_item");

    private static final By ITEM_NAME =
            By.className("inventory_item_name");

    private static final By ITEM_PRICE =
            By.className("inventory_item_price");

    private static final By ITEM_TOTAL =
            By.className("summary_subtotal_label");

    private static final By TAX =
            By.className("summary_tax_label");

    private static final By TOTAL =
            By.className("summary_total_label");

    private static final By FINISH_BUTTON =
            By.id("finish");

    private static final By CANCEL_BUTTON =
            By.id("cancel");


    // Constructor
    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }


    // Verify overview page is loaded
    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-step-two.html"
                    )
            );

            return isVisible(TITLE);

        } catch (TimeoutException e) {
            return false;
        }
    }


    // Read heading
    public String heading() {
        return getText(TITLE).trim();
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


    // Count products
    public int itemCount() {
        return driver.findElements(CART_ITEM).size();
    }


    // Read item total
    public BigDecimal itemTotal() {
        return parseSummaryAmount(
                getText(ITEM_TOTAL)
        );
    }


    // Read tax
    public BigDecimal tax() {
        return parseSummaryAmount(
                getText(TAX)
        );
    }


    // Read final total
    public BigDecimal total() {
        return parseSummaryAmount(
                getText(TOTAL)
        );
    }


    // Extract amount from summary label
    private BigDecimal parseSummaryAmount(String text) {

        String amount = text.substring(
                text.indexOf("$")
        );

        return PriceUtils.parse(amount);
    }


    // Finish checkout
    @Step("Finish the order")
    public CheckoutCompletePage finish() {

        click(FINISH_BUTTON);

        return new CheckoutCompletePage(driver);
    }


    // Cancel checkout
    @Step("Cancel checkout overview")
    public ProductsPage cancel() {

        click(CANCEL_BUTTON);

        return new ProductsPage(driver);
    }
}
