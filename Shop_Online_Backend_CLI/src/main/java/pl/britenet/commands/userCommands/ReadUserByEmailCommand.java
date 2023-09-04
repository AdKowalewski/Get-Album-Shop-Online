package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.User;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.Scanner;

public class ReadUserByEmailCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public ReadUserByEmailCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        System.out.print("Type user email: ");
        String emailToSearch = scanner.nextLine();

        User user = UserService.readUserByEmail(emailToSearch);

        if(user == null){
            System.out.println(Colors.YELLOW.getColor() + "There is no such user!" + Colors.RESET.getColor());
        } else {
            System.out.println("User " + user.getId() + " : " + user);
        }
    }
}
