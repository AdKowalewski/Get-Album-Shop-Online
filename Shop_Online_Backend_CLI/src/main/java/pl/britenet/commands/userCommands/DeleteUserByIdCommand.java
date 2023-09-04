package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.User;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.Scanner;

public class DeleteUserByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public DeleteUserByIdCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        System.out.print("Type id of user You want to delete: ");
        int indexToDelete = scanner.nextInt();
        User user = UserService.getUserObjectById(indexToDelete);
        if(user != null){
            UserService.deleteUserById(indexToDelete);
            System.out.println(Colors.GREEN.getColor() + "User " + indexToDelete + " deleted successfully!" +
                    Colors.RESET.getColor());
        } else {
            System.out.println(Colors.YELLOW.getColor() + "There is no such user!" + Colors.RESET.getColor());
        }
    }
}
