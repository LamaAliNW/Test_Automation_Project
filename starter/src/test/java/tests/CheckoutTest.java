
package tests;

import config.Config;
import core.BaseTest;
import io.qameta.allure.*;
import model.CartSummary;
import model.Product;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutInformationPage;
import pages.CheckoutOverviewPage;
import pages.ProductsPage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Epic("Swag Labs")
@Feature("Checkout")
public class CheckoutTest extends BaseTest {

    // Helper: Login
    private ProductsPage signIn() {
        return openLoginPage().loginAs(
                Config.get("standard.user"),
                Config.get("password")
        );
    }

    // Helper: Open checkout information page
    private CheckoutInformationPage openCheckout() {

        ProductsPage products = signIn();

        products.addToCart("Sauce Labs Backpack");

        CartPage cart = products.openCart();

        return cart.checkout();
    }

    // DataProvider: Missing checkout fields
    @DataProvider(name = "missingCheckoutFields")
    public Object[][] missingCheckoutFields() {

        return new Object[][]{
                {"", "Ali", "12345", "First Name is required"},
                {"Ahmed", "", "12345", "Last Name is required"},
                {"Ahmed", "Ali", "", "Postal Code is required"}
        };
    }

    // TEST 1: Missing checkout fields
    @Test(
            groups = "regression",
            dataProvider = "missingCheckoutFields"
    )
    @Story("Missing checkout fields are rejected")
    @Severity(SeverityLevel.CRITICAL)
    public void missingFieldsAreRejected(
            String firstName,
            String lastName,
            String postalCode,
            String expectedError) {

        CheckoutInformationPage info = openCheckout();

        info.fillInformation(
                firstName,
                lastName,
                postalCode
        );

        info.continueToOverview();

        Assert.assertTrue(
                info.isErrorDisplayed(),
                "Validation error should be displayed"
        );

        Assert.assertTrue(
                info.errorMessage().contains(expectedError),
                "Unexpected checkout validation error"
        );

        Assert.assertTrue(
                info.isLoaded(),
                "User should remain on checkout information page"
        );
    }

    // TEST 2: Verify checkout totals
    @Test(groups = "smoke")
    @Story("Checkout totals match calculated totals")
    @Severity(SeverityLevel.BLOCKER)
    public void overviewTotalsAreCorrect() {

        ProductsPage products = signIn();

        String firstName = "Ahmed";
        String lastName = "Ali";
        String postalCode = "12345";

        String product1 = "Sauce Labs Backpack";
        String product2 = "Sauce Labs Bike Light";

        // Read catalogue product names and prices
        List<String> names = products.productNames();

        List<BigDecimal> prices =
                products.productPricesAsBigDecimal();

        // Validate that selected products exist
        int index1 = names.indexOf(product1);
        int index2 = names.indexOf(product2);

        Assert.assertTrue(
                index1 >= 0,
                "Backpack was not found in catalogue"
        );

        Assert.assertTrue(
                index2 >= 0,
                "Bike Light was not found in catalogue"
        );

        // Create Product objects using:
        // Product(name, description, price)
        List<Product> selectedProducts = new ArrayList<>();

        selectedProducts.add(
                new Product(
                        product1,
                        "Backpack product",
                        prices.get(index1)
                )
        );

        selectedProducts.add(
                new Product(
                        product2,
                        "Bike Light product",
                        prices.get(index2)
                )
        );

        // Calculate expected totals independently
        CartSummary expected =
                new CartSummary(selectedProducts);

        // Add products to cart
        products.addToCart(product1);
        products.addToCart(product2);

        // Proceed to checkout overview
        CheckoutOverviewPage overview =
                products.openCart()
                        .checkout()
                        .fillInformation(
                                firstName,
                                lastName,
                                postalCode
                        )
                        .continueToOverview();

        Assert.assertTrue(
                overview.isLoaded(),
                "Checkout overview page did not load"
        );

        // Verify item total
        Assert.assertEquals(
                overview.itemTotal().compareTo(expected.itemTotal()),
                0,
                "Item total does not match calculated value"
        );

        // Verify tax
        Assert.assertEquals(
                overview.tax().compareTo(expected.tax()),
                0,
                "Tax does not match calculated value"
        );

        // Verify final total
        Assert.assertEquals(
                overview.total().compareTo(expected.total()),
                0,
                "Final total does not match calculated value"
        );
    }

    // TEST 3: Cancel checkout
    @Test(groups = "regression")
    @Story("Cancelling checkout preserves cart")
    @Severity(SeverityLevel.NORMAL)
    public void cancellingTheOrderReturnsToTheProducts() {

        CheckoutInformationPage info = openCheckout();

        CheckoutOverviewPage overview =
                info.fillInformation(
                        "Ahmed",
                        "Ali",
                        "12345"
                ).continueToOverview();

        ProductsPage products = overview.cancel();

        Assert.assertTrue(
                products.isLoaded(),
                "Cancelling checkout should return to products"
        );

        Assert.assertEquals(
                products.cartCount(),
                1,
                "Cart should retain its product"
        );

        Assert.assertTrue(
                products.openCart().containsProduct(
                        "Sauce Labs Backpack"
                ),
                "Product should remain in cart after cancellation"
        );
    }

    // TEST 4: Complete checkout
    @Test(groups = "regression")
    @Story("Finishing checkout confirms the order")
    @Severity(SeverityLevel.CRITICAL)
    public void finishingTheOrderConfirmsIt() {

        CheckoutInformationPage info = openCheckout();

        CheckoutOverviewPage overview =
                info.fillInformation(
                        "Ahmed",
                        "Ali",
                        "12345"
                ).continueToOverview();

        CheckoutCompletePage complete =
                overview.finish();

        Assert.assertTrue(
                complete.isLoaded(),
                "Checkout complete page did not load"
        );

        Assert.assertTrue(
                complete.isOrderSuccessful(),
                "Order confirmation message is incorrect"
        );

        Assert.assertEquals(
                complete.cartCount(),
                0,
                "Cart should be empty after completing order"
        );
    }
}
