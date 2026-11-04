
package tests;

import config.Config;
import core.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductsPage;

import java.math.BigDecimal;
import java.util.List;

@Epic("Swag Labs")
@Feature("Shopping Cart")
public class CartTest extends BaseTest {

    private ProductsPage signIn() {
        return openLoginPage().loginAs(
                Config.get("standard.user"),
                Config.get("password")
        );
    }

    // TEST 1: Cart shows added products
    @Test(groups = "smoke")
    @Story("Cart displays added products and quantities")
    @Severity(SeverityLevel.CRITICAL)
    public void cartShowsTheItemsThatWereAdded() {

        ProductsPage products = signIn();

        products.addToCart("Sauce Labs Backpack");
        products.addToCart("Sauce Labs Bike Light");

        CartPage cart = products.openCart();

        SoftAssert soft = new SoftAssert();

        soft.assertTrue(
                cart.isLoaded(),
                "Cart page did not load"
        );

        soft.assertEquals(
                cart.itemCount(),
                2,
                "Cart should contain two product rows"
        );

        soft.assertTrue(
                cart.containsProduct("Sauce Labs Backpack"),
                "Backpack is missing from cart"
        );

        soft.assertTrue(
                cart.containsProduct("Sauce Labs Bike Light"),
                "Bike Light is missing from cart"
        );

        soft.assertEquals(
                cart.quantities(),
                List.of(1, 1),
                "Each product should have quantity 1"
        );

        soft.assertAll();
    }

    // TEST 2: Cart prices match catalogue
    @Test(groups = "regression")
    @Story("Cart prices match catalogue prices")
    @Severity(SeverityLevel.CRITICAL)
    public void cartPricesMatchTheCatalogue() {

        ProductsPage products = signIn();

        List<String> names = products.productNames();
        List<BigDecimal> prices =
                products.productPricesAsBigDecimal();

        String firstProduct = "Sauce Labs Backpack";
        String secondProduct = "Sauce Labs Bike Light";

        BigDecimal firstPrice =
                prices.get(names.indexOf(firstProduct));

        BigDecimal secondPrice =
                prices.get(names.indexOf(secondProduct));

        products.addToCart(firstProduct);
        products.addToCart(secondProduct);

        CartPage cart = products.openCart();

        List<String> cartNames = cart.productNames();
        List<BigDecimal> cartPrices = cart.productPrices();

        Assert.assertEquals(
                cartPrices.get(cartNames.indexOf(firstProduct))
                        .compareTo(firstPrice),
                0,
                "Backpack price does not match catalogue"
        );

        Assert.assertEquals(
                cartPrices.get(cartNames.indexOf(secondProduct))
                        .compareTo(secondPrice),
                0,
                "Bike Light price does not match catalogue"
        );
    }

    // TEST 3: Removing an item updates cart and badge
    @Test(groups = "regression")
    @Story("Removing an item updates cart and badge")
    @Severity(SeverityLevel.CRITICAL)
    public void removingAnItemUpdatesTheCartAndTheBadge() {

        ProductsPage products = signIn();

        products.addToCart("Sauce Labs Backpack");
        products.addToCart("Sauce Labs Bike Light");

        CartPage cart = products.openCart();

        Assert.assertEquals(
                cart.cartCount(),
                2,
                "Cart badge should show two items"
        );

        cart.removeProduct("Sauce Labs Backpack");

        Assert.assertFalse(
                cart.containsProduct("Sauce Labs Backpack"),
                "Removed product is still in cart"
        );

        Assert.assertEquals(
                cart.itemCount(),
                1,
                "Cart should contain one product"
        );

        Assert.assertEquals(
                cart.cartCount(),
                1,
                "Cart badge should show one item"
        );
    }

    // TEST 4: Continue shopping preserves cart
    @Test(groups = "regression")
    @Story("Continue shopping preserves cart contents")
    @Severity(SeverityLevel.NORMAL)
    public void continueShoppingKeepsTheCart() {

        ProductsPage products = signIn();

        products.addToCart("Sauce Labs Backpack");

        CartPage cart = products.openCart();

        ProductsPage returnedProducts =
                cart.continueShopping();

        Assert.assertTrue(
                returnedProducts.isLoaded(),
                "Products page did not load after continue shopping"
        );

        Assert.assertEquals(
                returnedProducts.cartCount(),
                1,
                "Cart badge should still show one item"
        );

        CartPage reopenedCart =
                returnedProducts.openCart();

        Assert.assertTrue(
                reopenedCart.containsProduct("Sauce Labs Backpack"),
                "Product disappeared after continuing shopping"
        );
    }
}
