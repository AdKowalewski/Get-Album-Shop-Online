package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Client;
import pl.britenet.models.User;
import pl.britenet.services.ClientService;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.Scanner;

public class RegisterUserCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final UserService userService;
//
//    public RegisterUserCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        System.out.print("Type user name: ");
        String name = scanner.nextLine();
        System.out.print("Type user surname: ");
        String surname = scanner.nextLine();
        System.out.print("Type user email: ");
        String email = scanner.nextLine();
        System.out.print("Type user password: ");
        String password = scanner.nextLine();
        System.out.println();

        String regexForEmailValidation = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)" +
                "*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        if (name.equals("") || surname.equals("") || email.equals("") ||
                password.equals("")) {
            System.out.println(Colors.RED.getColor() + "All fields must be filled!" + Colors.RESET.getColor());
        } else if (!email.matches(regexForEmailValidation)) {
            System.out.println(Colors.RED.getColor() + "Invalid email address!" + Colors.RESET.getColor());
        } else {
            //try {
                UserService.registerUser(name, surname, email, password);
                System.out.println(Colors.GREEN.getColor() + "User " + name + " " + surname +
                        " registered successfully!" + Colors.RESET.getColor());
            //} catch (IllegalStateException e){
            //    System.out.println(Colors.RED.getColor() + "Email already taken!" + Colors.RESET.getColor());
            //}
            User newUser = UserService.getLastInsert();
            //Client newClient = ClientService.getLastInsert();
            UserService.loginUser(newUser);
            System.out.println(Colors.GREEN.getColor() + "You logged in successfully!" + Colors.RESET.getColor());
            System.out.println(Colors.BLUE.getColor() + "Hello " + name + " " + surname + "!" +
                    Colors.RESET.getColor());
        }
    }
}
