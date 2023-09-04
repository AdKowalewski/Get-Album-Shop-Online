package pl.britenet.commands.orderCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class OrderMainCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public OrderMainCommand(){
//        this.userService = new UserService();
//    }

    static Map<Integer, ICommand> commands = new HashMap<>();
    static {
        commands.put(1, new CreateOrderCommand());
        commands.put(2, new ReadAllOrdersCommand());
        commands.put(3, new ReadCurrentClientOrdersCommand());
        commands.put(4, new ReadNewestOrder());
        commands.put(5, new ReadOrderByIdCommand());
        commands.put(6, new UpdateOrderByIdCommand());
        commands.put(7, new DeleteOrderByIdCommand());
    }

    @Override
    public void execute() {
        while(true) {
            System.out.println(Colors.PURPLE.getColor() + "Choose preferred action from below:");
            System.out.println("1. Create order.");
            System.out.println("2. Read all orders.");
            System.out.println("3. Read Your orders.");
            System.out.println("4. Read Your newest order.");
            System.out.println("5. Read order by id.");
            System.out.println("6. Update order by id.");
            //System.out.println("7. Delete order by id.");
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

            if(chosenOption < 0 || chosenOption > 7){
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
            } else if (chosenOption == 0) {
                break;
            } else {
                if (UserService.isLoggedIn()) {
                    commands.get(chosenOption).execute();
                } else {
                    System.out.println(Colors.YELLOW.getColor() + "You must log in to perform this action!" +
                            Colors.RESET.getColor());
                }
            }
            System.out.println();
        }
    }
}
