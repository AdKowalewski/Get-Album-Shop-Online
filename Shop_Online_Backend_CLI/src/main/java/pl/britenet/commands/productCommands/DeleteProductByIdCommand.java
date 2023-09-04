package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class DeleteProductByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final ProductService productService;
//
//    public DeleteProductByIdCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of album You want to delete: ");
        int indexToDelete = scanner.nextInt();
        Product product = ProductService.getProductObjectById(indexToDelete);
        if(product != null){
            ProductService.deleteProductById(indexToDelete);
            System.out.println(Colors.GREEN.getColor() + "Product " + indexToDelete + " deleted successfully!" +
                    Colors.RESET.getColor());
        } else {
            System.out.println(Colors.YELLOW.getColor() + "There is no such product!" + Colors.RESET.getColor());
        }
    }
}
