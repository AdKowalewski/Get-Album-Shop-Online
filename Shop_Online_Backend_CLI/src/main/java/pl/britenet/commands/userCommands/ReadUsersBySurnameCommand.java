package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.User;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Scanner;

public class ReadUsersBySurnameCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public ReadUsersBySurnameCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        System.out.print("Type the surname: ");
        String surnamesToSearch = scanner.nextLine();

        List<User> usersBySurname = UserService.readUsersBySurname(surnamesToSearch);

        if(usersBySurname.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no users of given surname!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of users:");
            for (int i = 0; i < usersBySurname.size(); i++) {
                System.out.print("User " + usersBySurname.get(i).getId() + " : ");
                System.out.println(usersBySurname.get(i));
            }
        }
    }
}
