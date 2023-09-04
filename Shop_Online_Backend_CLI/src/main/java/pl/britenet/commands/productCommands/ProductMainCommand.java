package pl.britenet.commands.productCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ProductMainCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public ProductMainCommand(){
//        this.userService = new UserService();
//    }

    static Map<Integer, ICommand> commands = new HashMap<>();
    static {
        commands.put(1, new CreateProductCommand());
        commands.put(2, new ReadAllProductsCommand());
        commands.put(3, new ReadProductByIdCommand());
        commands.put(4, new UpdateProductOfGivenIdCommand());
        commands.put(8, new DeleteProductByIdCommand());
        commands.put(5, new ReadProductByTitleCommand());
        commands.put(6, new ReadProductsByArtistCommand());
        commands.put(7, new ReadProductsByGenreCommand());
    }

    @Override
    public void execute() {
        while(true) {
            System.out.println(Colors.PURPLE.getColor() + "Choose preferred action from below:");
            System.out.println("1. Create product.");
            System.out.println("2. Read all products.");
            System.out.println("3. Read specific product by id.");
            System.out.println("4. Update product.");
            System.out.println("5. Read specific product by album title.");
            System.out.println("6. Read products by album artist.");
            System.out.println("7. Read products by music genre.");
            //System.out.println("8. Delete product.");
            System.out.println("0. Back to menu.");
            System.out.println(Colors.RESET.getColor());

            System.out.print("Your choice: ");
            int chosenOption;
            try {
                chosenOption = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println();
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                break;
            }
            System.out.println();

            if(chosenOption < 0 || chosenOption > 8){
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
            } else if(chosenOption == 0) {
                break;
            } else if (chosenOption == 1 || chosenOption == 4 || chosenOption == 5) {
                if (UserService.isLoggedIn()) {
                    commands.get(chosenOption).execute();
                } else {
                    System.out.println(Colors.YELLOW.getColor() + "You must log in to perform this action!" + Colors.RESET.getColor());
                }
            } else {
                commands.get(chosenOption).execute();
            }
            System.out.println();
        }
    }
}
