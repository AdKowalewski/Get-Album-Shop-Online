package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.ProductService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class CreateProductCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final ProductService productService;
//
//    public CreateProductCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        System.out.print("Type name of album artist: ");
        String artist = scanner.nextLine();
        System.out.print("Type name of album title: ");
        String title = scanner.nextLine();
        System.out.print("Type name of album genre: ");
        String genre = scanner.nextLine();
        System.out.print("Type name of album label: ");
        String label = scanner.nextLine();
        System.out.print("Type album release year: ");
        String yearStr = scanner.nextLine();
        System.out.println();
        if (artist.equals("") || title.equals("") || genre.equals("") || label.equals("") ||
                yearStr.equals("")) {
            System.out.println(Colors.RED.getColor() + "All fields must be filled!" + Colors.RESET.getColor());
        } else {
            int year = Integer.parseInt(yearStr);
            ProductService.createProduct(artist, title, genre, label, year);
            System.out.println(Colors.GREEN.getColor() + "Product created successfully!" + Colors.RESET.getColor());
        }
    }
}
