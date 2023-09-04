package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class ReadProductByTitleCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final ProductService productService;
//
//    public ReadProductByTitleCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        System.out.print("Type title of album: ");
        String titleToSearch = scanner.nextLine();

        Product product = ProductService.readProductByTitle(titleToSearch);

        if(product == null) {
            System.out.println(Colors.YELLOW.getColor() + "There is no such product!" + Colors.RESET.getColor());
        } else {
            System.out.println("Product " + product.getId() + " : " + product);
        }
    }
}
