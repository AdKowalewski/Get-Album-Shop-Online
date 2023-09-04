package pl.britenet.commands.userCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.UserService;

public class LogoutUserCommand implements ICommand {

//    private final UserService userService;
//
//    public LogoutUserCommand(){
//        this.userService = new UserService();
//    }

    @Override
    public void execute() {
        UserService.logOutUser();
        System.out.println(Colors.GREEN.getColor() + "You are logged out!" + Colors.RESET.getColor());
    }
}
