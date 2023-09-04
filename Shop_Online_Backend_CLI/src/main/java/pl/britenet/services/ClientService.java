package pl.britenet.services;

import pl.britenet.database.DatabaseService;
import pl.britenet.models.Client;
import pl.britenet.models.builders.ClientBuilder;
import pl.britenet.models.builders.UserBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    private static final DatabaseService databaseService = DatabaseService.getInstance();

//    private final AddressService addressService;
//
//    public ClientService(){
//        this.addressService = new AddressService();
//    }

    public static void createClient(int id, String name, String surname){
        databaseService.performDML(String.format(
                        "INSERT INTO client (client_id, name, surname) VALUES (%d, '%s', '%s')",
                        id, name, surname
                )
        );
    }

    //may be deleted in the future
    public static List<Client> readAllClients(){
        return databaseService.performSQL(
                "SELECT * FROM client",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Client>();
                        while (resultSet.next()) {
                            items.add(new ClientBuilder()
                                    .setId(resultSet.getInt("client_id"))
                                    .setName(resultSet.getString("name"))
                                    .setSurname(resultSet.getString("surname"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static Client getClientObjectById(int id) {
        return databaseService.performSQL(
                "SELECT * FROM client WHERE client_id=" + id,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new ClientBuilder()
                                    .setId(resultSet.getInt("client_id"))
                                    .setName(resultSet.getString("name"))
                                    .setSurname(resultSet.getString("surname"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    //may be deleted in the future
    public static List<Client> readClientsByName(String name){
        return databaseService.performSQL(
                "SELECT * FROM client WHERE name LIKE \"%" + name + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Client>();
                        while (resultSet.next()) {
                            items.add(new ClientBuilder()
                                    .setId(resultSet.getInt("client_id"))
                                    .setName(resultSet.getString("name"))
                                    .setSurname(resultSet.getString("surname"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    //may be deleted in the future
    public static List<Client> readClientsBySurname(String surname){
        return databaseService.performSQL(
                "SELECT * FROM client WHERE surname LIKE \"%" + surname + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Client>();
                        while (resultSet.next()) {
                            items.add(new ClientBuilder()
                                    .setId(resultSet.getInt("client_id"))
                                    .setName(resultSet.getString("name"))
                                    .setSurname(resultSet.getString("surname"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static void updateClientOfGivenId(int id, String updatedName, String updatedSurname){
        final var oldClient = getClientObjectById(id);
        databaseService.performDML(String.format(
                "UPDATE client SET name='%s', surname='%s' WHERE client_id=%d",
                updatedName.equals("") ? oldClient.getName() : updatedName,
                updatedSurname.equals("") ? oldClient.getSurname() : updatedSurname,
                id
        ));
    }

    public static void deleteClientById(int id){
        AddressService.deleteAddressesByClientId(id);
        databaseService.performDML(String.format("DELETE FROM client WHERE client_id=%d", id));
    }

    public static Client getLastInsert(){
        return databaseService.performSQL(
                "SELECT * FROM client WHERE client_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new ClientBuilder()
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
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
