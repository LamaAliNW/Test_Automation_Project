
package tests;

import config.Config;
import core.BaseTest;
import io.qameta.allure.*;
import model.Customer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutCompletePage;
import pages.CheckoutInformationPage;
import pages.CheckoutOverviewPage;
import pages.ProductsPage;
import utils.CsvReader;

import java.util.List;

@Epic("Swag Labs")
@Feature("End-to-End")
public class E2ETest extends BaseTest {

    // Helper: Login
    private ProductsPage signIn() {
        return openLoginPage().loginAs(
                Config.get("standard.user"),
                Config.get("password")
        );
    }

    // DataProvider: Read customers from CSV
    @DataProvider(name = "customers")
    public Object[][] customers() {

        List<Customer> customerList =
                CsvReader.readCustomers("testdata/customers.csv");

        Object[][] data = new Object[customerList.size()][1];

        for (int i = 0; i < customerList.size(); i++) {
            data[i][0] = customerList.get(i);
        }

        return data;
    }

    // TEST 1: Complete purchase for every CSV customer
    @Test(
            groups = "e2e",
            dataProvider = "customers"
    )
    @Story("Customer completes a full purchase")
    @Severity(SeverityLevel.BLOCKER)
    public void customerCanCompleteAPurchase(Customer customer) {

        // Step 1: Login
        ProductsPage products = signIn();

        Assert.assertTrue(
                products.isLoaded(),
                "Products page did not load"
        );

        // Step 2: Add customer's products
        for (String productName : customer.getProducts()) {

            products.addToCart(productName);
        }

        Assert.assertEquals(
                products.cartCount(),
                customer.getProducts().size(),
                "Cart badge does not match selected products"
        );

        // Step 3: Open cart
        CartPage cart = products.openCart();

        Assert.assertTrue(
                cart.isLoaded(),
                "Cart page did not load"
        );

        Assert.assertEquals(
                cart.itemCount(),
                customer.getProducts().size(),
                "Cart item count is incorrect"
        );

        for (String productName : customer.getProducts()) {

            Assert.assertTrue(
                    cart.containsProduct(productName),
                    "Product missing from cart: " + productName
            );
        }

        // Step 4: Checkout information
        CheckoutInformationPage info = cart.checkout();

        Assert.assertTrue(
                info.isLoaded(),
                "Checkout information page did not load"
        );

        info.fillInformation(
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPostalCode()
        );

        // Step 5: Checkout overview
        CheckoutOverviewPage overview =
                info.continueToOverview();

        Assert.assertTrue(
                overview.isLoaded(),
                "Checkout overview page did not load"
        );

        Assert.assertEquals(
                overview.itemCount(),
                customer.getProducts().size(),
                "Checkout overview item count is incorrect"
        );

        for (String productName : customer.getProducts()) {

            Assert.assertTrue(
                    overview.productNames().contains(productName),
                    "Product missing from checkout overview: "
                            + productName
            );
        }

        // Step 6: Finish order
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
                "Cart should be empty after purchase"
        );
    }

    // TEST 2: Cancel checkout and keep basket
    @Test(groups = "e2e")
    @Story("Cancelling halfway keeps the basket")
    @Severity(SeverityLevel.CRITICAL)
    public void cancellingHalfwayKeepsTheBasket() {

        // Step 1: Login
        ProductsPage products = signIn();

        // Step 2: Add products
        products.addToCart("Sauce Labs Backpack");
        products.addToCart("Sauce Labs Bike Light");

        Assert.assertEquals(
                products.cartCount(),
                2,
                "Cart should contain two items"
        );

        // Step 3: Open cart and checkout
        CartPage cart = products.openCart();

        CheckoutInformationPage info = cart.checkout();

        // Step 4: Enter customer information
        CheckoutOverviewPage overview =
                info.fillInformation(
                        "Ahmed",
                        "Ali",
                        "12345"
                ).continueToOverview();

        Assert.assertTrue(
                overview.isLoaded(),
                "Checkout overview page did not load"
        );

        // Step 5: Cancel checkout
        ProductsPage returnedProducts =
                overview.cancel();

        Assert.assertTrue(
                returnedProducts.isLoaded(),
                "User did not return to products page"
        );

        // Step 6: Verify cart is preserved
        Assert.assertEquals(
                returnedProducts.cartCount(),
                2,
                "Cart badge should still show two items"
        );

        CartPage reopenedCart =
                returnedProducts.openCart();

        Assert.assertEquals(
                reopenedCart.itemCount(),
                2,
                "Cart should still contain two products"
        );

        Assert.assertTrue(
                reopenedCart.containsProduct("Sauce Labs Backpack"),
                "Backpack disappeared after cancellation"
        );

        Assert.assertTrue(
                reopenedCart.containsProduct("Sauce Labs Bike Light"),
                "Bike Light disappeared after cancellation"
        );
    }
}
