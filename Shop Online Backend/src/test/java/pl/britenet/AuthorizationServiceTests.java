package pl.britenet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;
import pl.britenet.authorization.AuthorizationController;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.User;
import pl.britenet.models.builders.UserBuilder;
import pl.britenet.services.ClientService;
import pl.britenet.services.UserService;

import java.util.Optional;

@SpringBootTest
public class AuthorizationServiceTests {

    DatabaseService databaseService = new DatabaseService();

    @Test
    void testIfRegisterUserWorksProperly(){
        UserService userService = new UserService(databaseService);
        //ClientService clientService = new ClientService(databaseService);

        userService.registerUser("Janusz", "Kowalski", "janusz.kowal@wp.pl", "cos");
        Optional<User> user = Optional.ofNullable(userService.getLastInsert());

        Assertions.assertTrue(user.isPresent());
        //user.get().setClient(clientService.getClientObjectById(user.get().getId()));
        Assertions.assertEquals("janusz.kowal@wp.pl", user.get().getEmail());
        Assertions.assertTrue(BCrypt.checkpw("cos", user.get().getPassword()));
        Assertions.assertEquals("Janusz", user.get().getClient().getName());
        Assertions.assertEquals("Kowalski", user.get().getClient().getSurname());
    }

    @Test
    void testIfLoginUserWorksProperly(){
        UserService userService = new UserService(databaseService);
        AuthorizationController controller = new AuthorizationController(userService);

        User user = new UserBuilder()
                .setEmail("adkowal@gmail.com")
                .setPassword("haslo123")
                .setClient(null)
                .build();

        controller.loginUser(user);

        Assertions.assertTrue(controller.getTokens().containsValue(userService.readUserByEmail(user.getEmail()).getId()));
    }

    @Test
    void testIfLogoutUserWorksProperly(){
        UserService userService = new UserService(databaseService);
        AuthorizationController controller = new AuthorizationController(userService);

        User user = new UserBuilder()
                .setEmail("adkowal@gmail.com")
                .setPassword("haslo123")
                .setClient(null)
                .build();

        controller.userLogout(userService.readUserByEmail(user.getEmail()).getId());

        Assertions.assertFalse(controller.getTokens().containsValue(userService.readUserByEmail(user.getEmail()).getId()));
    }
}
