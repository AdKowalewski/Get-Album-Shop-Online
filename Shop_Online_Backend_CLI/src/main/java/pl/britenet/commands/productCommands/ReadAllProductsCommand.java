package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;

import java.util.List;

public class ReadAllProductsCommand implements ICommand {

//    private final ProductService productService;
//
//    public ReadAllProductsCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        List<Product> products = ProductService.readAllProducts();
        if (products.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no any products!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of products:");
            for (int i = 0; i < products.size(); i++) {
                System.out.print("Product " + products.get(i).getId() + " : ");
                System.out.println(products.get(i));
            }
        }
    }
}
