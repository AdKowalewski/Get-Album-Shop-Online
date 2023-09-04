package pl.britenet.services;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.britenet.config.DatabaseConfig;
import pl.britenet.config.ServiceConfig;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.User;
import pl.britenet.models.builders.ClientBuilder;
import pl.britenet.models.builders.UserBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final DatabaseService databaseService;
    private boolean isLoggedIn;
    private User currentUser;

    private final ClientService clientService;
    DatabaseConfig databaseConfig = new DatabaseConfig();
    ServiceConfig serviceConfig = new ServiceConfig(databaseConfig.getDatabaseService());

    public UserService(DatabaseService databaseService){
        this.clientService = serviceConfig.getClientService();
        this.databaseService = databaseService;
    }

    public boolean isLoggedIn() {
        return this.isLoggedIn;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public void registerUser(String name, String surname, String email, String password){
        //this.clientService.createClient(name, surname);
        this.databaseService.performDML(String.format(
                        "INSERT INTO user (email, password) VALUES ('%s', '%s')",
                        email, BCrypt.hashpw(password, BCrypt.gensalt(12))
                )
        );
        int id = getLastInsertId();
        this.clientService.createClient(id, name, surname);
    }

    public boolean doesEmailExist(String email){
        return readUserByEmail(email) != null;
    }

    public void loginUser(User user){
            this.isLoggedIn = true;
            this.currentUser = user;
    }

    public List<User> readAllUsers(){
        return this.databaseService.performSQL(
                "SELECT u.user_id, c.client_id as c_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id",
                resultSet -> {
                    try {
                        final var items = new ArrayList<User>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
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

    public User getUserObjectById(int id) {
        return this.databaseService.performSQL(
                "SELECT u.user_id, c.client_id as c_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE u.user_id=" + id,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
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

    public User getLastInsert() {
        return this.databaseService.performSQL(
                "SELECT u.user_id, c.client_id as c_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE u.user_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
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

    public int getLastInsertId() {
        return this.databaseService.performSQL(
                "SELECT u.user_id as last_id FROM user u WHERE u.user_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return resultSet.getInt("last_id");
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<User> readUsersByName(String name){
        return this.databaseService.performSQL(
                "SELECT u.user_id, c.client_id as c_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE c.name LIKE \"%" + name + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<User>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
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

    public List<User> readUsersBySurname(String surname){
        return this.databaseService.performSQL(
                "SELECT u.user_id, c.client_id as c_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE c.surname LIKE \"%" + surname + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<User>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
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

    public User readUserByEmail(String email){
        return this.databaseService.performSQL(
                "SELECT u.user_id, c.client_id as c_id, c.name as cname, c.surname as csurname, u.email, u.password FROM user u JOIN client c ON c.client_id = u.user_id WHERE u.email LIKE \"%" + email + "%\"",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
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

    public User readUserByEmailWithoutClient(String email){
        return this.databaseService.performSQL(
                "SELECT * FROM user u WHERE u.email LIKE \"%" + email + "%\"",
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

    public Set<String> readAllEmails(){
        return this.databaseService.performSQL(
                "SELECT u.email FROM user u",
                resultSet -> {
                    try{
                        final var emails = new HashSet<String>();
                        if(resultSet.next()){
                            emails.add(resultSet.getString("email"));
                        }
                        return emails;
                    } catch (SQLException e){
                        throw new IllegalStateException(e);
                    }
                }
        ).get();
    }

    public void updateUserOfGivenId(int id, String updatedName, String updatedSurname, String updatedEmail,
                                           String updatedPassword){
        final var oldUser = getUserObjectById(id);
        this.databaseService.performDML(String.format(
                "UPDATE user SET email='%s', password='%s' WHERE user_id=%d",
                updatedEmail.equals("") ? oldUser.getEmail() : updatedEmail,
                updatedPassword.equals("") ? oldUser.getPassword() : BCrypt.hashpw(updatedPassword, BCrypt.gensalt(12)),
                id
        ));
        this.clientService.updateClientOfGivenId(id, updatedName, updatedSurname);
    }

    public void deleteUserById(int id){
        this.databaseService.performDML(String.format("DELETE FROM user WHERE user_id=%d", id));
        this.clientService.deleteClientById(id);
    }

    public void logOutUser(){
        this.isLoggedIn = false;
        this.currentUser = null;
    }
}