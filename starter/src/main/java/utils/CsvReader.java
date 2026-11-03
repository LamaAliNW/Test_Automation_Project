
package utils;

import exceptions.FrameworkException;
import model.Customer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class CsvReader {

    private CsvReader() {
    }

    public static List<Customer> readCustomers(String fileName) {

        List<Customer> customers = new ArrayList<>();

        try (InputStream input = CsvReader.class
                .getClassLoader()
                .getResourceAsStream(fileName)) {

            if (input == null) {
                throw new FrameworkException(
                        "CSV file not found: " + fileName);
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8))) {

                String header = reader.readLine();

                if (header == null) {
                    throw new FrameworkException(
                            "CSV file is empty: " + fileName);
                }

                String line;
                int lineNumber = 1;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    if (line.isBlank()) {
                        continue;
                    }

                    String[] values = line.split(",", -1);

                    if (values.length != 4) {
                        throw new FrameworkException(
                                "Invalid CSV row at line " + lineNumber
                                        + " in " + fileName);
                    }

                    List<String> products = Arrays.stream(
                                    values[3].split("\\|", -1))
                            .map(String::trim)
                            .toList();

                    customers.add(new Customer(
                            values[0].trim(),
                            values[1].trim(),
                            values[2].trim(),
                            products
                    ));
                }
            }

            return customers;

        } catch (FrameworkException e) {
            throw e;
        } catch (Exception e) {
            throw new FrameworkException(
                    "Failed to read CSV file: " + fileName, e);
        }
    }
}
