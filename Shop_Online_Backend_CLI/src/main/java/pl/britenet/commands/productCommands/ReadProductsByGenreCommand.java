package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;
import pl.britenet.services.ScannerService;

import java.util.List;
import java.util.Scanner;

public class ReadProductsByGenreCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final ProductService productService;
//
//    public ReadProductsByGenreCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        System.out.print("Type the name of genre: ");
        String genreToSearch = scanner.nextLine();

        List<Product> albumsByGenre = ProductService.readProductsByGenre(genreToSearch);

        if(albumsByGenre.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no albums in such genre!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of products:");
            for (int i = 0; i < albumsByGenre.size(); i++) {
                System.out.print("Product " + albumsByGenre.get(i).getId() + " : ");
                System.out.println(albumsByGenre.get(i));
            }
        }
    }
}
