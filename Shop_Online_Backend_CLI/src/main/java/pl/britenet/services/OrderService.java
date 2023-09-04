package pl.britenet.services;

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

public class OrderService {

    private static final DatabaseService databaseService = DatabaseService.getInstance();

//    private final OrderItemService orderItemService;
//    private final UserService userService;
//    private final ClientService clientService;
//    private final AddressService addressService;
//
//    public OrderService(){
//        this.orderItemService = new OrderItemService();
//        this.userService = new UserService();
//        this.clientService = new ClientService();
//        this.addressService = new AddressService();
//    }

    public static void createOrder(Client client, Address address, Map<Integer, Integer> products){
        databaseService.performDML(String.format(
                        "INSERT INTO orders (client_id, address_id) VALUES (%d, %d)",
                        client.getId(), address.getId()
                )
        );
        Integer orderId = getLastInsertId();
        for(Map.Entry<Integer, Integer> entry : products.entrySet()){
            OrderItemService.createOrderItem(orderId, entry.getKey(), entry.getValue());
        }
    }

    private static Integer getLastInsertId(){
        return databaseService.performSQL(
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

    public static Order getLastInsert(){
        return databaseService.performSQL(
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
                                    .setProducts(OrderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
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

    public static List<Order> readAllOrders(){
        return databaseService.performSQL(
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
                                            .setProducts(OrderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
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

    public static List<Order> readCurrentClientOrders(int id){
        return databaseService.performSQL(
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
                                            .setProducts(OrderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
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

    public static Order getOrderObjectById(int id){
        return databaseService.performSQL(
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
                                    .setProducts(OrderItemService.readOrderItemsByOrderId(resultSet.getInt("order_id")))
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

    public static void updateOrderById(int id, Map<Integer, Integer> products, Address address){
        Order oldOrder = getOrderObjectById(id);
        databaseService.performDML(String.format(
                "UPDATE orders SET modified=CURRENT_TIMESTAMP() WHERE order_id=%d", id
        ));
        OrderItemService.deleteOrderItemByOrderId(id);
        for(Map.Entry<Integer, Integer> entry : products.entrySet()){
            OrderItemService.createOrderItem(id, entry.getKey(), entry.getValue());
        }
        OrderItemService.deleteOrderItemByOrderIdIfQuantityEqualsZero(id);
        AddressService.updateAddressById(
                address.getId(),
                address.getCountry(),
                address.getTown(),
                address.getStreet(),
                address.getStreetNumber().toString(),
                address.getLocalNumber().toString(),
                address.getPostalCode()
        );
    }

    public static void deleteOrderById(int id){
        databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d", id));
        databaseService.performDML(String.format("DELETE FROM orders WHERE order_id=%d", id));
    }

    public static void deleteOrdersByClientId(int id){
        List<Order> clientOrders = readCurrentClientOrders(id);
        for(int i = 0; i < clientOrders.size(); i++){
            deleteOrderById(clientOrders.get(i).getId());
        }
    }

    public static Order getNewestOrder() {
        return getOrderObjectById(getNewestOrderId(ClientService.getClientObjectById(UserService.getCurrentUser().getId())));
    }

    public static int getNewestOrderId(Client currentClient) {
        return databaseService.performSQL(
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