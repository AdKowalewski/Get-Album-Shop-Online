package pl.britenet.services;

import pl.britenet.database.DatabaseService;
import pl.britenet.models.Address;
import pl.britenet.models.builders.AddressBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressService {

    private static final DatabaseService databaseService = DatabaseService.getInstance();

    public static void createAddress(String country, String town, String street, Integer streetNumber, Integer localNumber,
                                     String postalCode, int clientId){
        databaseService.performDML(String.format(
                        "INSERT INTO address (country, town, street, street_number, local_number, postal_code, client_id) " +
                                "VALUES ('%s', '%s', '%s', %d, %d, '%s', %d)",
                        country, town, street, streetNumber, localNumber, postalCode, clientId
                )
        );
    }

    public static List<Address> readAllAddresses(){
        return databaseService.performSQL(
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

    public static Address getAddressObjectById(int id){
        return databaseService.performSQL(
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

    public static List<Address> readClientAddresses(int id){
        return databaseService.performSQL(
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

    public static void updateAddressById(int id, String updatedCountry, String updatedTown, String updatedStreet,
                                         String updatedStreetNumberStr, String updatedLocalNumberStr, String updatedPostalCode){
        final var oldAddress = getAddressObjectById(id);
        databaseService.performDML(String.format(
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

    public static void deleteAddressById(int id){
        databaseService.performDML(String.format("DELETE FROM address WHERE address_id=%d", id));
    }

    public static void deleteAddressesByClientId(int id){
        databaseService.performDML(String.format("DELETE FROM address WHERE client_id=%d", id));
    }

    public static Address getLastInsert(){
        return databaseService.performSQL(
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
