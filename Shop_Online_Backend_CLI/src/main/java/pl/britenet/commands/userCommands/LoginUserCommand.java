package pl.britenet.commands.userCommands;

import org.mindrot.jbcrypt.BCrypt;
import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.User;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.Scanner;

public class LoginUserCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public LoginUserCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        if(UserService.isLoggedIn()){
            System.out.println(Colors.YELLOW.getColor() + "You are already logged in!" + Colors.RESET.getColor());
        } else {
            System.out.print("Type user email: ");
            String email = scanner.nextLine();
            System.out.print("Type user password: ");
            String password = scanner.nextLine();
            System.out.println();

            User user = UserService.readUserByEmail(email);

            //user.getPassword().equals(password) <- version before BCrypt in lines 33, 35
            //BCrypt.checkpw(password, user.getPassword()) <- version using BCrypt

            if (!UserService.doesEmailExist(email)) {
                System.out.println(Colors.RED.getColor() + "Invalid email!" + Colors.RESET.getColor());
            } else if (user != null && !BCrypt.checkpw(password, user.getPassword())) {
                System.out.println(Colors.RED.getColor() + "Invalid password!" + Colors.RESET.getColor());
            } else if (user != null && BCrypt.checkpw(password, user.getPassword())) {
                UserService.loginUser(user);
                System.out.println(Colors.GREEN.getColor() + "You logged in successfully!" + Colors.RESET.getColor());
                System.out.println(Colors.BLUE.getColor() + "Hello " + user.getClient().getName() + " " + user.getClient().getSurname() + "!" +
                        Colors.RESET.getColor());
            }
        }
    }
}
