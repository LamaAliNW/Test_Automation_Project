package model;


import java.math.BigDecimal;
import java.util.Objects;

public final class Product implements Comparable<Product> {

    private final String name;
    private final String description;
    private final BigDecimal price;

    public Product(String name, String description, BigDecimal price) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Product description cannot be empty");
        }

        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }

        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int compareTo(Product other) {
        return this.price.compareTo(other.price);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Product other)) {
            return false;
        }

        return name.equals(other.name)
                && description.equals(other.description)
                && price.compareTo(other.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}