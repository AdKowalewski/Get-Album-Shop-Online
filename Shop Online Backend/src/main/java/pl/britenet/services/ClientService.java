package pl.britenet.services;

import org.springframework.stereotype.Service;
import pl.britenet.config.DatabaseConfig;
import pl.britenet.config.ServiceConfig;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.Client;
import pl.britenet.models.builders.ClientBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private final DatabaseService databaseService;

    private final AddressService addressService;
    DatabaseConfig databaseConfig = new DatabaseConfig();
    ServiceConfig serviceConfig = new ServiceConfig(databaseConfig.getDatabaseService());

    public ClientService(DatabaseService databaseService){
        this.addressService = serviceConfig.getAddressService();
        this.databaseService = databaseService;
    }

    public void createClient(int id, String name, String surname){
        this.databaseService.performDML(String.format(
                        "INSERT INTO client (client_id, name, surname) VALUES (%d, '%s', '%s')",
                        id, name, surname
                )
        );
    }

    //may be deleted in the future
    public List<Client> readAllClients(){
        return this.databaseService.performSQL(
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

    public Client getClientObjectById(int id) {
        return this.databaseService.performSQL(
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
    public List<Client> readClientsByName(String name){
        return this.databaseService.performSQL(
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
    public List<Client> readClientsBySurname(String surname){
        return this.databaseService.performSQL(
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

    public void updateClientOfGivenId(int id, String updatedName, String updatedSurname){
        final var oldClient = getClientObjectById(id);
        this.databaseService.performDML(String.format(
                "UPDATE client SET name='%s', surname='%s' WHERE client_id=%d",
                updatedName.equals("") ? oldClient.getName() : updatedName,
                updatedSurname.equals("") ? oldClient.getSurname() : updatedSurname,
                id
        ));
    }

    public void deleteClientById(int id){
        this.addressService.deleteAddressesByClientId(id);
        this.databaseService.performDML(String.format("DELETE FROM client WHERE client_id=%d", id));
    }
}
