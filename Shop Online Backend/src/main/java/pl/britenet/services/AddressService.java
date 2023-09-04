package pl.britenet.services;

import org.springframework.stereotype.Service;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.Address;
import pl.britenet.models.builders.AddressBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    private final DatabaseService databaseService;

    public AddressService(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    public void createAddress(String country, String town, String street, Integer streetNumber, Integer localNumber,
                                     String postalCode, int clientId){
        this.databaseService.performDML(String.format(
                        "INSERT INTO address (country, town, street, street_number, local_number, postal_code, client_id) " +
                                "VALUES ('%s', '%s', '%s', %d, %d, '%s', %d)",
                        country, town, street, streetNumber, localNumber, postalCode, clientId
                )
        );
    }

    public List<Address> readAllAddresses(){
        return this.databaseService.performSQL(
                "SELECT * FROM address",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Address>();
                        while (resultSet.next()) {
                            items.add(new AddressBuilder()
                                    .setId(resultSet.getInt("address_id"))
                                    .setCountry(resultSet.getString("country"))
                                    .setTown(resultSet.getString("town"))
                                    .setStreet(resultSet.getString("street"))
                                    .setStreetNumber(resultSet.getInt("street_number"))
                                    .setLocalNumber(resultSet.getInt("local_number"))
                                    .setPostalCode(resultSet.getString("postal_code"))
                                    .setClientId(resultSet.getInt("client_id"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public Address getAddressObjectById(int id){
        return this.databaseService.performSQL(
                "SELECT * FROM address WHERE address_id = " + id,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new AddressBuilder()
                                    .setId(resultSet.getInt("address_id"))
                                    .setCountry(resultSet.getString("country"))
                                    .setTown(resultSet.getString("town"))
                                    .setStreet(resultSet.getString("street"))
                                    .setStreetNumber(resultSet.getInt("street_number"))
                                    .setLocalNumber(resultSet.getInt("local_number"))
                                    .setPostalCode(resultSet.getString("postal_code"))
                                    .setClientId(resultSet.getInt("client_id"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<Address> readClientAddresses(int id){
        return this.databaseService.performSQL(
                "SELECT * FROM address WHERE client_id=" + id,
                resultSet -> {
                    try {
                        final var items = new ArrayList<Address>();
                        while (resultSet.next()) {
                            items.add(new AddressBuilder()
                                    .setId(resultSet.getInt("address_id"))
                                    .setCountry(resultSet.getString("country"))
                                    .setTown(resultSet.getString("town"))
                                    .setStreet(resultSet.getString("street"))
                                    .setStreetNumber(resultSet.getInt("street_number"))
                                    .setLocalNumber(resultSet.getInt("local_number"))
                                    .setPostalCode(resultSet.getString("postal_code"))
                                    .setClientId(resultSet.getInt("client_id"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public void updateAddressById(int id, String updatedCountry, String updatedTown, String updatedStreet,
                                         String updatedStreetNumberStr, String updatedLocalNumberStr, String updatedPostalCode){
        final var oldAddress = getAddressObjectById(id);
        this.databaseService.performDML(String.format(
                "UPDATE address SET country='%s', town='%s', street='%s', street_number=%d, local_number=%d, postal_code='%s' WHERE address_id=%d",
                updatedCountry.equals("") ? oldAddress.getCountry() : updatedCountry,
                updatedTown.equals("") ? oldAddress.getTown() : updatedTown,
                updatedStreet.equals("") ? oldAddress.getStreet() : updatedStreet,
                updatedStreetNumberStr.equals("") ? oldAddress.getStreetNumber() : Integer.parseInt(updatedStreetNumberStr),
                updatedLocalNumberStr.equals("") ? oldAddress.getLocalNumber() : Integer.parseInt(updatedLocalNumberStr),
                updatedPostalCode.equals("") ? oldAddress.getPostalCode() : updatedPostalCode,
                id
        ));
    }

    public void deleteAddressById(int id){
        this.databaseService.performDML(String.format("DELETE FROM address WHERE address_id=%d", id));
    }

    public void disableAddressById(int id){
        this.databaseService.performDML(String.format(
                "UPDATE address SET client_id=NULL WHERE address_id=%d", id
        ));
    }

    public void deleteAddressesByClientId(int id){
        this.databaseService.performDML(String.format("DELETE FROM address WHERE client_id=%d", id));
    }

    public Address getLastInsert(){
        return this.databaseService.performSQL(
                "SELECT * FROM address WHERE address_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new AddressBuilder()
                                    .setId(resultSet.getInt("address_id"))
                                    .setCountry(resultSet.getString("country"))
                                    .setTown(resultSet.getString("town"))
                                    .setStreet(resultSet.getString("street"))
                                    .setStreetNumber(resultSet.getInt("street_number"))
                                    .setLocalNumber(resultSet.getInt("local_number"))
                                    .setPostalCode(resultSet.getString("postal_code"))
                                    .setClientId(resultSet.getInt("client_id"))
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
