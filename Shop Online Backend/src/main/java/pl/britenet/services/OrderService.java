package pl.britenet.services;

import org.springframework.stereotype.Service;
import pl.britenet.config.DatabaseConfig;
import pl.britenet.config.ServiceConfig;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.Address;
import pl.britenet.models.Client;
import pl.britenet.models.Order;
import pl.britenet.models.builders.AddressBuilder;
import pl.britenet.models.builders.ClientBuilder;
import pl.britenet.models.builders.OrderBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final DatabaseService databaseService;

    private final OrderItemService orderItemService;
    private final UserService userService;
    private final ClientService clientService;
    private final AddressService addressService;
    DatabaseConfig databaseConfig = new DatabaseConfig();
    ServiceConfig serviceConfig = new ServiceConfig(databaseConfig.getDatabaseService());

    public OrderService(DatabaseService databaseService){
        this.orderItemService = serviceConfig.getOrderItemService();
        this.userService = serviceConfig.getUserService();
        this.clientService = serviceConfig.getClientService();
        this.addressService = serviceConfig.getAddressService();
        this.databaseService = databaseService;
    }

    public void createOrder(int clientId, int addressId, Map<Integer, Integer> products){
        this.databaseService.performDML(String.format(
                        "INSERT INTO orders (client_id, address_id) VALUES (%d, %d)",
                        clientId, addressId
                )
        );
        Integer orderId = getLastInsertId();
        for(Map.Entry<Integer, Integer> entry : products.entrySet()){
            this.orderItemService.createOrderItem(orderId, entry.getKey(), entry.getValue());
        }
    }

    private Integer getLastInsertId(){
        return this.databaseService.performSQL(
                "SELECT order_id FROM orders WHERE order_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return resultSet.getInt("order_id");
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public Order getLastInsert(){
        return this.databaseService.performSQL(
                "SELECT o.order_id, c.client_id as c_id, a.client_id as aclient, a.address_id as a_id, c.name as cname, c.surname as csurname," +
                        "a.country as acountry, a.town as atown, a.street as astreet, a.street_number as streetNum, a.local_number as localNum, a.postal_code as postalCode," +
                        "o.created, o.modified FROM orders o JOIN client c ON c.client_id=o.client_id JOIN address a ON a.address_id=o.address_id" +
                        " WHERE o.order_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            final var address = new AddressBuilder()
                                    .setId(resultSet.getInt("a_id"))
                                    .setCountry(resultSet.getString("acountry"))
                                    .setTown(resultSet.getString("atown"))
                                    .setStreet(resultSet.getString("astreet"))
                                    .setStreetNumber(resultSet.getInt("streetNum"))
                                    .setLocalNumber(resultSet.getInt("localNum"))
                                    .setPostalCode(resultSet.getString("postalCode"))
                                    .setClientId(resultSet.getInt("aclient"))
                                    .build();
                            return new OrderBuilder()
                                    .setId(resultSet.getInt("order_id"))
                                    .setClient(client)
                                    .setAddress(address)
                                    .setProducts(this.orderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
                                    .setCreated(resultSet.getTimestamp("created"))
                                    .setModified(resultSet.getTimestamp("modified"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<Order> readAllOrders(){
        return this.databaseService.performSQL(
                "SELECT o.order_id, c.client_id as c_id, a.client_id as aclient, a.address_id as a_id, c.name as cname, c.surname as csurname," +
                        "a.country as acountry, a.town as atown, a.street as astreet, a.street_number as streetNum, a.local_number as localNum, a.postal_code as postalCode," +
                        "o.created, o.modified FROM orders o JOIN client c ON c.client_id=o.client_id JOIN address a ON a.address_id=o.address_id",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Order>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            final var address = new AddressBuilder()
                                    .setId(resultSet.getInt("a_id"))
                                    .setCountry(resultSet.getString("acountry"))
                                    .setTown(resultSet.getString("atown"))
                                    .setStreet(resultSet.getString("astreet"))
                                    .setStreetNumber(resultSet.getInt("streetNum"))
                                    .setLocalNumber(resultSet.getInt("localNum"))
                                    .setPostalCode(resultSet.getString("postalCode"))
                                    .setClientId(resultSet.getInt("aclient"))
                                    .build();
                            items.add(
                                    new OrderBuilder()
                                            .setId(resultSet.getInt("order_id"))
                                            .setClient(client)
                                            .setAddress(address)
                                            .setProducts(this.orderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
                                            .setCreated(resultSet.getTimestamp("created"))
                                            .setModified(resultSet.getTimestamp("modified"))
                                            .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<Order> readCurrentClientOrders(int id){
        return this.databaseService.performSQL(
                "SELECT o.order_id, c.client_id as c_id, a.client_id as aclient, a.address_id as a_id, c.name as cname, c.surname as csurname," +
                        "a.country as acountry, a.town as atown, a.street as astreet, a.street_number as streetNum, a.local_number as localNum, a.postal_code as postalCode," +
                        "o.created, o.modified FROM orders o JOIN client c ON c.client_id=o.client_id JOIN address a ON a.address_id=o.address_id" +
                        " WHERE o.client_id=" + id,
                resultSet -> {
                    try {
                        final var items = new ArrayList<Order>();
                        while (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            final var address = new AddressBuilder()
                                    .setId(resultSet.getInt("a_id"))
                                    .setCountry(resultSet.getString("acountry"))
                                    .setTown(resultSet.getString("atown"))
                                    .setStreet(resultSet.getString("astreet"))
                                    .setStreetNumber(resultSet.getInt("streetNum"))
                                    .setLocalNumber(resultSet.getInt("localNum"))
                                    .setPostalCode(resultSet.getString("postalCode"))
                                    .setClientId(resultSet.getInt("aclient"))
                                    .build();
                            items.add(
                                    new OrderBuilder()
                                            .setId(resultSet.getInt("order_id"))
                                            .setClient(client)
                                            .setAddress(address)
                                            .setProducts(this.orderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
                                            .setCreated(resultSet.getTimestamp("created"))
                                            .setModified(resultSet.getTimestamp("modified"))
                                            .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public Order getOrderObjectById(int id){
        return this.databaseService.performSQL(
                "SELECT o.order_id, c.client_id as c_id, a.client_id as aclient, a.address_id as a_id, c.name as cname, c.surname as csurname," +
                        "a.country as acountry, a.town as atown, a.street as astreet, a.street_number as streetNum, a.local_number as localNum, a.postal_code as postalCode," +
                        "o.created, o.modified FROM orders o JOIN client c ON c.client_id=o.client_id JOIN address a ON a.address_id=o.address_id" +
                        " WHERE o.order_id=" + id,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            final var client = new ClientBuilder()
                                    .setId(resultSet.getInt("c_id"))
                                    .setName(resultSet.getString("cname"))
                                    .setSurname(resultSet.getString("csurname"))
                                    .build();
                            final var address = new AddressBuilder()
                                    .setId(resultSet.getInt("a_id"))
                                    .setCountry(resultSet.getString("acountry"))
                                    .setTown(resultSet.getString("atown"))
                                    .setStreet(resultSet.getString("astreet"))
                                    .setStreetNumber(resultSet.getInt("streetNum"))
                                    .setLocalNumber(resultSet.getInt("localNum"))
                                    .setPostalCode(resultSet.getString("postalCode"))
                                    .setClientId(resultSet.getInt("aclient"))
                                    .build();
                            return new OrderBuilder()
                                    .setId(resultSet.getInt("order_id"))
                                    .setClient(client)
                                    .setAddress(address)
                                    .setProducts(this.orderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
                                    .setCreated(resultSet.getTimestamp("created"))
                                    .setModified(resultSet.getTimestamp("modified"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public void updateOrderById(int id, Map<Integer, Integer> products, Address address){
        Order oldOrder = getOrderObjectById(id);
        this.databaseService.performDML(String.format(
                "UPDATE orders SET modified=CURRENT_TIMESTAMP() WHERE order_id=%d", id
        ));
        this.orderItemService.deleteOrderItemByOrderId(id);
        for(Map.Entry<Integer, Integer> entry : products.entrySet()){
            this.orderItemService.createOrderItem(id, entry.getKey(), entry.getValue());
        }
        this.orderItemService.deleteOrderItemByOrderIdIfQuantityEqualsZero(id);
        this.addressService.updateAddressById(
                address.getId(),
                address.getCountry(),
                address.getTown(),
                address.getStreet(),
                address.getStreetNumber().toString(),
                address.getLocalNumber().toString(),
                address.getPostalCode()
        );
    }

    public void deleteOrderById(int id){
        this.databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d", id));
        this.databaseService.performDML(String.format("DELETE FROM orders WHERE order_id=%d", id));
    }

    public void deleteOrdersByClientId(int id){
        List<Order> clientOrders = readCurrentClientOrders(id);
        for(int i = 0; i < clientOrders.size(); i++){
            deleteOrderById(clientOrders.get(i).getId());
        }
    }

    public Order getNewestOrder() {
        return getOrderObjectById(getNewestOrderId(this.clientService.getClientObjectById(this.userService.getCurrentUser().getId())));
    }

    public int getNewestOrderId(Client currentClient) {
        return this.databaseService.performSQL(
                String.format(
                        "SELECT Max(order_id) as id FROM orders WHERE client_id = %d", currentClient.getId()
                ),
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return resultSet.getInt("id");
                        }
                        else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }
}