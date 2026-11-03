package tests;


import config.Config;
import core.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ProductsPage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Epic("Swag Labs")
@Feature("Products")
public class ProductsTest extends BaseTest {

    private ProductsPage signIn() {
        return openLoginPage().loginAs(Config.get("standard.user"), Config.get("password"));
    }

    @Test(groups = "smoke")
    @Story("The catalogue lists every product")
    @Severity(SeverityLevel.CRITICAL)
    public void allProductsAreListed() {
        ProductsPage products = signIn();

        Assert.assertTrue(products.isLoaded(), "products page did not load");
        Assert.assertEquals(products.productCount(), 6, "wrong number of products");
    }

    @Test(groups = "smoke")
    @Story("Adding an item updates the cart badge")
    @Severity(SeverityLevel.BLOCKER)
    public void addingItemsUpdatesTheCartBadge() {
        ProductsPage products = signIn();
        Assert.assertEquals(products.cartCount(), 0, "cart should start empty");

        products.addToCart("Sauce Labs Backpack");
        Assert.assertEquals(products.cartCount(), 1, "badge did not show one item");

        products.addToCart("Sauce Labs Bike Light");
        Assert.assertEquals(products.cartCount(), 2, "badge did not show two items");
    }

    @Test(groups = "regression")
    @Story("Sorting by price puts the cheapest first")
    @Severity(SeverityLevel.NORMAL)
    public void sortingByPriceLowToHigh() {
        List<Double> prices = signIn().sortBy("lohi").productPrices();

        List<Double> sorted = new ArrayList<>(prices);
        sorted.sort(Comparator.naturalOrder());
        Assert.assertEquals(prices, sorted, "products are not sorted cheapest first");
    }

    @Test(groups = "regression")
    @Story("Sorting by name Z to A reverses the catalogue")
    @Severity(SeverityLevel.MINOR)
    public void sortingByNameZtoA() {
        List<String> names = signIn().sortBy("za").productNames();

        List<String> sorted = new ArrayList<>(names);
        sorted.sort(Comparator.reverseOrder());
        Assert.assertEquals(names, sorted, "products are not in reverse alphabetical order");
    }
}

