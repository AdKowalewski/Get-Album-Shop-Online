package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.User;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Scanner;

public class ReadUsersByNameCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public ReadUsersByNameCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        System.out.print("Type the name: ");
        String namesToSearch = scanner.nextLine();

        List<User> usersByName = UserService.readUsersByName(namesToSearch);

        if(usersByName.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no users of given name!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of users:");
            for (int i = 0; i < usersByName.size(); i++) {
                System.out.print("User " + usersByName.get(i).getId() + " : ");
                System.out.println(usersByName.get(i));
            }
        }
    }
}
