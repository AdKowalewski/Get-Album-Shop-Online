package pl.britenet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.test.context.SpringBootTest;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.User;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTests {

    DatabaseService databaseService = new DatabaseService();

    @Test
    void testIfThereIsUserByGivenId(){
        UserService userService = new UserService(databaseService);

        Optional<User> user = Optional.ofNullable(userService.getUserObjectById(2));

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("adkowal@gmail.com", user.get().getEmail());
        Assertions.assertTrue(BCrypt.checkpw("haslo123", user.get().getPassword()));
        Assertions.assertEquals("Adam", user.get().getClient().getName());
        Assertions.assertEquals("Kowalewski", user.get().getClient().getSurname());
    }

    @Test
    void testIfThereIsNotUserByGivenId(){
        UserService userService = new UserService(databaseService);

        Optional<User> user = Optional.ofNullable(userService.getUserObjectById(2137));

        Assertions.assertFalse(user.isPresent());
    }

    @Test
    void testIfThereIsUserByGivenEmail(){
        UserService userService = new UserService(databaseService);

        Optional<User> user = Optional.ofNullable(userService.readUserByEmail("adkowal@gmail.com"));

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("adkowal@gmail.com", user.get().getEmail());
        Assertions.assertTrue(BCrypt.checkpw("haslo123", user.get().getPassword()));
        Assertions.assertEquals("Adam", user.get().getClient().getName());
        Assertions.assertEquals("Kowalewski", user.get().getClient().getSurname());
    }

    @Test
    void testIfThereIsNotUserByGivenEmail(){
        UserService userService = new UserService(databaseService);

        Optional<User> user = Optional.ofNullable(userService.readUserByEmail("dfgsfgdg"));

        Assertions.assertFalse(user.isPresent());
    }

    @Test
    void testIfThereAreUsersByGivenName(){
        UserService userService = new UserService(databaseService);

        Optional<List<User>> users = Optional.ofNullable(userService.readUsersByName("Adam"));

        Assertions.assertTrue(users.isPresent());
        Assertions.assertEquals("Adam", users.get().get(0).getClient().getName());
    }

    @Test
    void testIfThereAreNotUsersByGivenName(){
        UserService userService = new UserService(databaseService);

        Optional<List<User>> users = Optional.ofNullable(userService.readUsersByName("dgdfgdad"));

        Assertions.assertFalse(users.isPresent());
    }

    @Test
    void testIfThereAreUsersByGivenSurname(){
        UserService userService = new UserService(databaseService);

        Optional<List<User>> users = Optional.ofNullable(userService.readUsersBySurname("Kowalewski"));

        Assertions.assertTrue(users.isPresent());
        Assertions.assertEquals("Kowalewski", users.get().get(0).getClient().getSurname());
    }

    @Test
    void testIfThereAreNotUsersByGivenSurname(){
        UserService userService = new UserService(databaseService);

        Optional<List<User>> users = Optional.ofNullable(userService.readUsersBySurname("dgdfgdad"));

        Assertions.assertFalse(users.isPresent());
    }

    @Test
    void testIfUserUpdateWorksProperly(){
        UserService userService = new UserService(databaseService);

        userService.updateUserOfGivenId(2, "", "", "Adkowal@gmail.com", "");
        Optional<User> user = Optional.ofNullable(userService.getUserObjectById(2));

        Assertions.assertTrue(user.isPresent());
        Assertions.assertEquals("Adkowal@gmail.com", user.get().getEmail());
        Assertions.assertTrue(BCrypt.checkpw("haslo123", user.get().getPassword()));
        Assertions.assertEquals("Adam", user.get().getClient().getName());
        Assertions.assertEquals("Kowalewski", user.get().getClient().getSurname());
    }

    @Test
    void testIfUserDeleteWorksProperly(){
        UserService userService = new UserService(databaseService);

        userService.deleteUserById(51);
        Optional<User> user = Optional.ofNullable(userService.getUserObjectById(51));

        Assertions.assertFalse(user.isPresent());
    }
}
