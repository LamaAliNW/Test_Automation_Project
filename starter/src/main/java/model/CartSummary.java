package model;

import config.Config;
import utils.PriceUtils;

import java.math.BigDecimal;
import java.util.List;

public class CartSummary {

    private final List<Product> products;
    private final BigDecimal taxRate;

    public CartSummary(List<Product> products) {
        this(products, BigDecimal.valueOf(Config.taxRate()));
    }

    public CartSummary(List<Product> products, BigDecimal taxRate) {
        if (products == null || products.contains(null)) {
            throw new IllegalArgumentException("Products cannot be null");
        }

        if (taxRate == null || taxRate.signum() < 0) {
            throw new IllegalArgumentException("Tax rate cannot be negative");
        }

        this.products = List.copyOf(products);
        this.taxRate = taxRate;
    }

    public BigDecimal itemTotal() {
        BigDecimal sum = BigDecimal.ZERO;

        for (Product product : products) {
            sum = sum.add(product.getPrice());
        }

        return PriceUtils.round(sum);
    }

    public BigDecimal tax() {
        return PriceUtils.round(
                itemTotal().multiply(taxRate)
        );
    }

    public BigDecimal total() {
        return PriceUtils.round(
                itemTotal().add(tax())
        );
    }
}
