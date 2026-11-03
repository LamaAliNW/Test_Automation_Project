package exceptions;
import java.util.List;
public class ProductNotFoundException extends FrameworkException {

    public ProductNotFoundException(String productName, List<String> availableProducts) {
        super("Product '" + productName + "' was not found. Available products: "
                + availableProducts);
    }
}
