
package tests;

import config.Config;
import core.BaseTest;
import exceptions.ProductNotFoundException;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ProductDetailsPage;
import pages.ProductsPage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Epic("Swag Labs")
@Feature("Products")
public class ProductsTest extends BaseTest {

    // Login helper
    private ProductsPage signIn() {
        return openLoginPage().loginAs(
                Config.get("standard.user"),
                Config.get("password")
        );
    }


    // TEST 1: All products are listed
    @Test(groups = "smoke")
    @Story("The catalogue lists every product")
    @Severity(SeverityLevel.CRITICAL)
    public void allProductsAreListed() {

        ProductsPage products = signIn();

        Assert.assertTrue(
                products.isLoaded(),
                "Products page did not load"
        );

        Assert.assertEquals(
                products.productCount(),
                6,
                "Wrong number of products"
        );

        for (BigDecimal price :
                products.productPricesAsBigDecimal()) {

            Assert.assertTrue(
                    price.compareTo(BigDecimal.ZERO) > 0,
                    "Product price must be greater than zero"
            );
        }
    }


    // TEST 2: Add and remove products
    @Test(groups = "smoke")
    @Story("Adding and removing products updates the cart badge")
    @Severity(SeverityLevel.BLOCKER)
    public void addingAndRemovingUpdatesTheCartBadge() {

        ProductsPage products = signIn();

        Assert.assertEquals(
                products.cartCount(),
                0,
                "Cart should start empty"
        );

        // Add first product
        products.addToCart("Sauce Labs Backpack");

        Assert.assertEquals(
                products.cartCount(),
                1,
                "Badge should show one item"
        );

        Assert.assertTrue(
                products.isProductAdded("Sauce Labs Backpack"),
                "Button should change to Remove"
        );

        // Add second product
        products.addToCart("Sauce Labs Bike Light");

        Assert.assertEquals(
                products.cartCount(),
                2,
                "Badge should show two items"
        );

        // Remove first product
        products.removeFromCart("Sauce Labs Backpack");

        Assert.assertEquals(
                products.cartCount(),
                1,
                "Badge should show one item after removal"
        );

        Assert.assertFalse(
                products.isProductAdded("Sauce Labs Backpack"),
                "Button should return to Add to cart"
        );
    }


    // TEST 3: Sort prices low to high
    @Test(groups = "regression")
    @Story("Sorting by price puts the cheapest first")
    @Severity(SeverityLevel.NORMAL)
    public void sortingByPriceLowToHigh() {

        List<Double> prices =
                signIn()
                        .sortBy("lohi")
                        .productPrices();

        List<Double> sorted = new ArrayList<>(prices);

        sorted.sort(Comparator.naturalOrder());

        Assert.assertEquals(
                prices,
                sorted,
                "Products are not sorted cheapest first"
        );
    }


    // TEST 4: Sort names Z to A
    @Test(groups = "regression")
    @Story("Sorting by name Z to A reverses the catalogue")
    @Severity(SeverityLevel.MINOR)
    public void sortingByNameZtoA() {

        List<String> names =
                signIn()
                        .sortBy("za")
                        .productNames();

        List<String> sorted = new ArrayList<>(names);

        sorted.sort(Comparator.reverseOrder());

        Assert.assertEquals(
                names,
                sorted,
                "Products are not in reverse alphabetical order"
        );
    }


    // TEST 5: Product details match catalogue
    @Test(groups = "regression")
    @Story("Product details match the catalogue")
    @Severity(SeverityLevel.CRITICAL)
    public void productDetailsMatchTheCatalogue() {

        ProductsPage products = signIn();

        String productName = "Sauce Labs Backpack";

        List<String> names = products.productNames();
        List<BigDecimal> prices =
                products.productPricesAsBigDecimal();

        int index = names.indexOf(productName);

        Assert.assertTrue(
                index >= 0,
                "Product was not found in catalogue"
        );

        BigDecimal expectedPrice = prices.get(index);

        ProductDetailsPage details =
                products.openProductDetails(productName);

        Assert.assertTrue(
                details.isLoaded(),
                "Product details page did not load"
        );

        Assert.assertEquals(
                details.productName(),
                productName,
                "Product name does not match catalogue"
        );

        Assert.assertEquals(
                details.productPrice().compareTo(expectedPrice),
                0,
                "Product price does not match catalogue"
        );
    }


    // TEST 6: Unknown product throws custom exception
    @Test(
            groups = "regression",
            expectedExceptions = ProductNotFoundException.class
    )
    @Story("Unknown products raise a clear error")
    @Severity(SeverityLevel.NORMAL)
    public void unknownProductRaisesAClearError() {

        ProductsPage products = signIn();

        products.addToCart("Unknown Test Product");
    }
}
