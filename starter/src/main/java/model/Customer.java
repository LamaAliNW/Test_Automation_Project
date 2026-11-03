
package model;

import java.util.List;

public final class Customer {

    private final String firstName;
    private final String lastName;
    private final String postalCode;
    private final List<String> products;

    public Customer(String firstName, String lastName,
                    String postalCode, List<String> products) {

        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }

        if (postalCode == null || postalCode.isBlank()) {
            throw new IllegalArgumentException("Postal code cannot be empty");
        }

        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products cannot be empty");
        }

        if (products.stream().anyMatch(
                product -> product == null || product.isBlank())) {
            throw new IllegalArgumentException("Product names cannot be empty");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.products = List.copyOf(products);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public List<String> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", products=" + products +
                '}';
    }
}
