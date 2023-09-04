package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class UserMainCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public UserMainCommand(){
//        this.userService = new UserService();
//    }

    static Map<Integer, ICommand> commands = new HashMap<>();
    static {
        commands.put(1, new RegisterUserCommand());
        commands.put(2, new LoginUserCommand());
        commands.put(3, new ReadAllUsersCommand());
        commands.put(4, new ReadUserByIdCommand());
        commands.put(5, new ReadUsersByNameCommand());
        commands.put(6, new ReadUsersBySurnameCommand());
        commands.put(7, new ReadUserByEmailCommand());
        commands.put(8, new UpdateUserOfGivenIdCommand());
        commands.put(10, new DeleteUserByIdCommand());
        commands.put(9, new LogoutUserCommand());
    }

    @Override
    public void execute() {
        while(true) {
            System.out.println(Colors.PURPLE.getColor() + "Choose preferred action from below:");
            System.out.println("1. Register user.");
            System.out.println("2. Login user.");
            System.out.println("3. Read all users.");
            System.out.println("4. Read user by id.");
            System.out.println("5. Read users by name.");
            System.out.println("6. Read users by surname.");
            System.out.println("7. Read user by email.");
            System.out.println("8. Update user.");
            System.out.println("9. Logout.");
            //System.out.println("10. Delete user.");
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

            if(chosenOption < 0 || chosenOption > 10){
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
            } else if (chosenOption == 0) {
                break;
            } else if (chosenOption >= 3 && chosenOption <= 9) {
                if (UserService.isLoggedIn()) {
                    commands.get(chosenOption).execute();
                } else {
                    System.out.println(Colors.YELLOW.getColor() + "You must log in to perform this action!" +
                            Colors.RESET.getColor());
                }
            } else {
                commands.get(chosenOption).execute();
            }
            System.out.println();
        }
    }
}
