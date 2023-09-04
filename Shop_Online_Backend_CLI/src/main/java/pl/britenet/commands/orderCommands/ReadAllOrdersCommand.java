package pl.britenet.commands.orderCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Order;
import pl.britenet.models.Product;
import pl.britenet.services.OrderItemService;
import pl.britenet.services.OrderService;

import java.util.List;
import java.util.Map;

public class ReadAllOrdersCommand implements ICommand {

//    private final OrderService orderService;
//    private final OrderItemService orderItemService;
//
//    public ReadAllOrdersCommand(){
//        this.orderService = new OrderService();
//        this.orderItemService = new OrderItemService();
//    }

    @Override
    public void execute() {
        List<Order> orders = OrderService.readAllOrders();
        if (orders.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no any orders!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of orders:");
            for (int i = 0; i < orders.size(); i++) {
                System.out.println("Order " + orders.get(i).getId() + ":");
                System.out.println(orders.get(i));
                Map<Product, Integer> listOfProducts = OrderItemService.readProductsWithQuantitiesByOrderId(orders.get(i).getId());
                System.out.println("Products: [");
                for(Map.Entry<Product, Integer> entry : listOfProducts.entrySet()){
                    System.out.println(" * Product " + entry.getKey().getId() + " : " + entry.getKey() + ", Quantity: " + entry.getValue());
                }
                System.out.println("]");
                System.out.println();
            }
        }
    }
}
