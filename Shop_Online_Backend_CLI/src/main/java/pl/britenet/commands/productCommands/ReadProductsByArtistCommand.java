package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;
import pl.britenet.services.ScannerService;

import java.util.List;
import java.util.Scanner;

public class ReadProductsByArtistCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final ProductService productService;
//
//    public ReadProductsByArtistCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        System.out.print("Type the name of artist: ");
        String artistToSearch = scanner.nextLine();

        List<Product> albumsByArtist = ProductService.readProductsByArtist(artistToSearch);

        if(albumsByArtist.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no albums of this artist!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of products:");
            for (int i = 0; i < albumsByArtist.size(); i++) {
                System.out.print("Product " + albumsByArtist.get(i).getId() + " : ");
                System.out.println(albumsByArtist.get(i));
            }
        }
    }
}
