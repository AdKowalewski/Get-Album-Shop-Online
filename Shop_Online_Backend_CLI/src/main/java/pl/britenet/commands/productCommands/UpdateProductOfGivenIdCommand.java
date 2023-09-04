package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.ProductService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class UpdateProductOfGivenIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final ProductService productService;
//
//    public UpdateProductOfGivenIdCommand() {
//        this.productService = new ProductService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of album You want to update: ");
        int indexToUpdate = scanner.nextInt();
        System.out.println();
        scanner.nextLine();
        System.out.print("Type new name of album artist: ");
        String updatedArtist = scanner.nextLine();
        System.out.print("Type new name of album title: ");
        String updatedTitle = scanner.nextLine();
        System.out.print("Type new name of album genre: ");
        String updatedGenre = scanner.nextLine();
        System.out.print("Type new name of album label: ");
        String updatedLabel = scanner.nextLine();
        System.out.print("Type new album release year: ");
        String updatedYearStr = scanner.nextLine();
        System.out.println();
        ProductService.updateProductOfGivenId(indexToUpdate, updatedArtist, updatedTitle, updatedGenre,
                updatedLabel, updatedYearStr);
        System.out.println(Colors.GREEN.getColor() + "Product " + indexToUpdate + " updated successfully!" +
                Colors.RESET.getColor());
    }
}
