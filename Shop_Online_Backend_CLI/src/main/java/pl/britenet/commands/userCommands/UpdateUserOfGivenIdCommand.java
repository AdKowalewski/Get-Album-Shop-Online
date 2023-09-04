package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.Scanner;

public class UpdateUserOfGivenIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public UpdateUserOfGivenIdCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        System.out.print("Type id of user You want to update: ");
        int indexToUpdate = scanner.nextInt();
        System.out.println();
        scanner.nextLine();
        System.out.print("Type new user name: ");
        String updatedName = scanner.nextLine();
        System.out.print("Type new user surname: ");
        String updatedSurname = scanner.nextLine();
        System.out.print("Type new user email: ");
        String updatedEmail = scanner.nextLine();
        System.out.print("Type new user password: ");
        String updatedPassword = scanner.nextLine();
        System.out.println();
        UserService.updateUserOfGivenId(indexToUpdate, updatedName, updatedSurname, updatedEmail, updatedPassword);
        System.out.println(Colors.GREEN.getColor() + "User/client " + indexToUpdate + " updated successfully!" +
                Colors.RESET.getColor());
    }
}
