package pl.britenet.services;

import org.mindrot.jbcrypt.BCrypt;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.User;
import pl.britenet.models.builders.ClientBuilder;
import pl.britenet.models.builders.UserBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final DatabaseService databaseService = DatabaseService.getInstance();
    private static boolean isLoggedIn;
    private static User currentUser;

//    private final ClientService clientService;
//
//    public UserService(){
//        this.clientService = new ClientService();
//    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void registerUser(String name, String surname, String email, String password){
        databaseService.performDML(String.format(
                        "INSERT INTO user (email, password) VALUES ('%s', '%s')",
                        email, BCrypt.hashpw(password, BCrypt.gensalt(12))
                )
        );
        User newUser = getLastInsert();
        ClientService.createClient(newUser.getId(), name, surname);
    }

    public static boolean doesEmailExist(String email){
        return readUserByEmail(email) != null;
    }

    public static void loginUser(User user){
            isLoggedIn = true;
            currentUser = user;
    }

    public static List<User> readAllUsers(){
        return databaseService.performSQL(
                "SELECT u.user_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id",
                resultSet -> {
                    try {
                        final var items = new ArrayList<User>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            items.add(
                                    new UserBuilder()
                                    .setId(resultSet.getInt("user_id"))
                                    .setEmail(resultSet.getString("email"))
                                    .setPassword(resultSet.getString("password"))
                                    .setClient(client)
                                            .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static User getUserObjectById(int id) {
        return databaseService.performSQL(
                "SELECT u.user_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE u.user_id=" + id,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            return new UserBuilder()
                                    .setId(resultSet.getInt("user_id"))
                                    .setEmail(resultSet.getString("email"))
                                    .setPassword(resultSet.getString("password"))
                                    .setClient(client)
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static List<User> readUsersByName(String name){
        return databaseService.performSQL(
                "SELECT u.user_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE c.name LIKE \"%" + name + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<User>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            items.add(new UserBuilder()
                                    .setId(resultSet.getInt("user_id"))
                                    .setEmail(resultSet.getString("email"))
                                    .setPassword(resultSet.getString("password"))
                                    .setClient(client)
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static List<User> readUsersBySurname(String surname){
        return databaseService.performSQL(
                "SELECT u.user_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE c.surname LIKE \"%" + surname + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<User>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            items.add(new UserBuilder()
                                    .setId(resultSet.getInt("user_id"))
                                    .setEmail(resultSet.getString("email"))
                                    .setPassword(resultSet.getString("password"))
                                    .setClient(client)
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static User readUserByEmail(String email){
        return databaseService.performSQL(
                "SELECT u.user_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE u.email LIKE \"%" + email + "%\"",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            return new UserBuilder()
                                    .setId(resultSet.getInt("user_id"))
                                    .setEmail(resultSet.getString("email"))
                                    .setPassword(resultSet.getString("password"))
                                    .setClient(client)
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static void updateUserOfGivenId(int id, String updatedName, String updatedSurname, String updatedEmail,
                                           String updatedPassword){
        ClientService.updateClientOfGivenId(id, updatedName, updatedSurname);
        final var oldUser = getUserObjectById(id);
        databaseService.performDML(String.format(
                "UPDATE user SET email='%s', password='%s' WHERE user_id=%d",
                updatedEmail.equals("") ? oldUser.getEmail() : updatedEmail,
                updatedPassword.equals("") ? BCrypt.hashpw(oldUser.getPassword(), BCrypt.gensalt(12)) : BCrypt.hashpw(updatedPassword, BCrypt.gensalt(12)),
                id
        ));
    }

    public static void deleteUserById(int id){
        //ClientService.deleteClientById(id);
        databaseService.performDML(String.format("DELETE FROM user WHERE user_id=%d", id));
        ClientService.deleteClientById(id);

    }

    public static void logOutUser(){
        isLoggedIn = false;
        currentUser = null;
    }

    public static User getLastInsert(){
        return databaseService.performSQL(
                "SELECT * FROM user WHERE user_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new UserBuilder()
                                    .setId(resultSet.getInt("user_id"))
                                    .setEmail(resultSet.getString("email"))
                                    .setPassword(resultSet.getString("password"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }
}