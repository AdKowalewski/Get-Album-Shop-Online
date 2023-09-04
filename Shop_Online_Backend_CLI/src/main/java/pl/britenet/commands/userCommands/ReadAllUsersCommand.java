package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.User;
import pl.britenet.services.UserService;

import java.util.List;

public class ReadAllUsersCommand implements ICommand {

//    private final UserService userService;
//
//    public ReadAllUsersCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        List<User> users = UserService.readAllUsers();
        if (users.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no any users!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of users:");
            for (int i = 0; i < users.size(); i++) {
                System.out.print("User " + users.get(i).getId() + " : ");
                System.out.println(users.get(i));
            }
        }
    }
}
