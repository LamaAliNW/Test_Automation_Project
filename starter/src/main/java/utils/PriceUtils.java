package utils;


import java.math.BigDecimal;
import java.math.RoundingMode;

public final class PriceUtils {

    private PriceUtils() {
    }

    public static BigDecimal parse(String priceText) {
        if (priceText == null || priceText.isBlank()) {
            throw new IllegalArgumentException("Price cannot be empty");
        }

        String cleaned = priceText.replace("$", "").trim();

        return new BigDecimal(cleaned);
    }

    public static BigDecimal round(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}