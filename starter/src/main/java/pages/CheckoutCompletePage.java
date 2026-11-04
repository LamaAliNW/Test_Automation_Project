
package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutCompletePage extends BasePage {

    // Locators
    private static final By TITLE =
            By.className("title");

    private static final By COMPLETE_HEADER =
            By.className("complete-header");

    private static final By COMPLETE_TEXT =
            By.className("complete-text");

    private static final By BACK_HOME_BUTTON =
            By.id("back-to-products");


    // Constructor
    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }


    // Verify completion page is loaded
    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-complete.html"
                    )
            );

            return isVisible(COMPLETE_HEADER);

        } catch (TimeoutException e) {
            return false;
        }
    }


    // Read page heading
    public String heading() {
        return getText(TITLE).trim();
    }


    // Read confirmation message
    public String confirmationMessage() {
        return getText(COMPLETE_HEADER).trim();
    }


    // Read completion description
    public String confirmationDescription() {
        return getText(COMPLETE_TEXT).trim();
    }


    // Check success message
    public boolean isOrderSuccessful() {

        return confirmationMessage()
                .equalsIgnoreCase(
                        "Thank you for your order!"
                );
    }


    // Return to products
    @Step("Back to home after checkout")
    public ProductsPage backHome() {

        click(BACK_HOME_BUTTON);

        return new ProductsPage(driver);
    }
}
