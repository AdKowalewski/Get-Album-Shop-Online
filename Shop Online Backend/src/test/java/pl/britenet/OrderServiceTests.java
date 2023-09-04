package pl.britenet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.Address;
import pl.britenet.models.Client;
import pl.britenet.models.Order;
import pl.britenet.models.builders.AddressBuilder;
import pl.britenet.models.builders.ClientBuilder;
import pl.britenet.services.OrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class OrderServiceTests {

    DatabaseService databaseService = new DatabaseService();

    @Test
    void testIfOrderCreateWorksProperly(){
        OrderService orderService = new OrderService(databaseService);

//        Client client = new ClientBuilder()
//                .setId(51)
//                .setName("Tomasz")
//                .setSurname("Tomaszowski")
//                .build();
//
//        Address address = new AddressBuilder()
//                .setId(19)
//                .setCountry("Poland")
//                .setTown("Białystok")
//                .setStreet("Lipowa")
//                .setStreetNumber(12)
//                .setLocalNumber(23)
//                .setPostalCode("34")
//                .setClientId(51)
//                .build();

        Map<Integer, Integer> products = new HashMap<>();
        products.put(7, 1);

        orderService.createOrder(51, 19, products);
        Optional<Order> order = Optional.ofNullable(orderService.getLastInsert());

        Assertions.assertTrue(order.isPresent());
        Assertions.assertEquals("Tomasz", order.get().getClient().getName());
        Assertions.assertEquals("Tomaszowski", order.get().getClient().getSurname());
    }

    @Test
    void testIfListOfAllOrdersIsReadProperly(){
        OrderService orderService = new OrderService(databaseService);

        Optional<List<Order>> orders = Optional.ofNullable(orderService.readAllOrders());

        Assertions.assertTrue(orders.isPresent());
    }

    @Test
    void testIfThereIsOrderByGivenId(){
        OrderService orderService = new OrderService(databaseService);

        Optional<Order> order = Optional.ofNullable(orderService.getOrderObjectById(1));

        Assertions.assertTrue(order.isPresent());
        Assertions.assertEquals("Adam", order.get().getClient().getName());
        Assertions.assertEquals("Kowalewski", order.get().getClient().getSurname());
    }

    @Test
    void testIfThereIsNotOrderByGivenId(){
        OrderService orderService = new OrderService(databaseService);

        Optional<Order> order = Optional.ofNullable(orderService.getOrderObjectById(2137));

        Assertions.assertFalse(order.isPresent());
    }

    @Test
    void testIfThereAreOrdersOfGivenUserId(){
        OrderService orderService = new OrderService(databaseService);

        Optional<List<Order>> orders = Optional.ofNullable(orderService.readCurrentClientOrders(2));

        Assertions.assertTrue(orders.isPresent());
        Assertions.assertEquals("Adam", orders.get().get(0).getClient().getName());
        Assertions.assertEquals("Kowalewski", orders.get().get(0).getClient().getSurname());
    }

    @Test
    void testIfThereAreNotOrdersOfGivenUserId(){
        OrderService orderService = new OrderService(databaseService);

        Optional<List<Order>> orders = Optional.ofNullable(orderService.readCurrentClientOrders(2137));

        Assertions.assertFalse(orders.isPresent());
    }

    @Test
    void testIfOrderDeleteByIdWorksProperly(){
        OrderService orderService = new OrderService(databaseService);

        orderService.deleteOrderById(27);
        Optional<Order> order = Optional.ofNullable(orderService.getOrderObjectById(27));

        Assertions.assertFalse(order.isPresent());
    }
}
