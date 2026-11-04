
package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutInformationPage extends BasePage {

    // Locators
    private static final By TITLE =
            By.className("title");

    private static final By FIRST_NAME =
            By.id("first-name");

    private static final By LAST_NAME =
            By.id("last-name");

    private static final By POSTAL_CODE =
            By.id("postal-code");

    private static final By CONTINUE_BUTTON =
            By.id("continue");

    private static final By CANCEL_BUTTON =
            By.id("cancel");

    private static final By ERROR_MESSAGE =
            By.cssSelector("h3[data-test='error']");


    // Constructor
    public CheckoutInformationPage(WebDriver driver) {
        super(driver);
    }


    // Verify checkout information page is loaded
    public boolean isLoaded() {
        try {
            wait.until(
                    ExpectedConditions.urlContains(
                            "checkout-step-one.html"
                    )
            );

            return isVisible(FIRST_NAME);

        } catch (TimeoutException e) {
            return false;
        }
    }


    // Read heading
    public String heading() {
        return getText(TITLE).trim();
    }


    // Fill checkout information
    @Step("Fill checkout information")
    public CheckoutInformationPage fillInformation(
            String firstName,
            String lastName,
            String postalCode) {

        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);

        return this;
    }


    // Continue to checkout overview
    @Step("Continue to checkout overview")
    public CheckoutOverviewPage continueToOverview() {

        click(CONTINUE_BUTTON);

        return new CheckoutOverviewPage(driver);
    }


    // Read validation error
    public String errorMessage() {
        return getText(ERROR_MESSAGE).trim();
    }


    // Check if error is visible
    public boolean isErrorDisplayed() {
        return isVisible(ERROR_MESSAGE);
    }


    // Cancel checkout
    @Step("Cancel checkout information")
    public CartPage cancel() {

        click(CANCEL_BUTTON);

        return new CartPage(driver);
    }
}
