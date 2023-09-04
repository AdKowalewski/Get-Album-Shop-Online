package pl.britenet.commands.addressCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class AddressMainCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public AddressMainCommand(){
//        this.userService = new UserService();
//    }

    static Map<Integer, ICommand> commands = new HashMap<>();
    static {
        commands.put(1, new CreateAddressCommand());
        commands.put(2, new ReadAllAddressesCommand());
        commands.put(3, new ReadAddressByIdCommand());
        commands.put(4, new ReadCurrentClientAddressesCommand());
        commands.put(5, new UpdateAddressByIdCommand());
        commands.put(6, new DeleteAddressByIdCommand());
        commands.put(7, new DeleteAddressesByClientIdCommand());
    }

    @Override
    public void execute() {
        while(true) {
            System.out.println(Colors.PURPLE.getColor() + "Choose preferred action from below:");
            System.out.println("1. Create address.");
            System.out.println("2. Read all addresses.");
            System.out.println("3. Read specific address by id.");
            System.out.println("4. Read Your addresses.");
            System.out.println("5. Update address.");
            //System.out.println("6. Delete address.");
            //System.out.println("7. Delete addresses of given client id.");
            System.out.println("0. Back to menu.");
            System.out.println(Colors.RESET.getColor());

            int chosenOption;
            System.out.print("Your choice: ");
            try {
                chosenOption = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println();
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                break;
            }
            System.out.println();

            if(chosenOption < 0 || chosenOption > 7){
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
            } else if (chosenOption == 0){
                break;
            } else {
                if (UserService.isLoggedIn()) {
                    commands.get(chosenOption).execute();
                } else {
                    System.out.println(Colors.YELLOW.getColor() + "You must log in to perform this action!" + Colors.RESET.getColor());
                }
            }
        }
    }
}
