package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class ReadProductByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final ProductService productService;
//
//    public ReadProductByIdCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of album: ");
        int index = scanner.nextInt();

        Product product = ProductService.getProductObjectById(index);

        if(product == null) {
            System.out.println(Colors.YELLOW.getColor() + "There is no such product!" + Colors.RESET.getColor());
        } else {
            System.out.println("Product " + product.getId() + " : " + product);
        }
    }
}
