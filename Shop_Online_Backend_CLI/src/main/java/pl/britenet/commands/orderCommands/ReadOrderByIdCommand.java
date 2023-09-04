package pl.britenet.commands.orderCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Order;
import pl.britenet.models.Product;
import pl.britenet.services.OrderItemService;
import pl.britenet.services.OrderService;
import pl.britenet.services.ScannerService;

import java.util.Map;
import java.util.Scanner;

public class ReadOrderByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final OrderService orderService;
//    private final OrderItemService orderItemService;
//
//    public ReadOrderByIdCommand(){
//        this.orderService = new OrderService();
//        this.orderItemService = new OrderItemService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of order: ");
        int index = scanner.nextInt();

        Order order = OrderService.getOrderObjectById(index);

        if(order == null) {
            System.out.println(Colors.YELLOW.getColor() + "There is no such order!" + Colors.RESET.getColor());
        } else {
            System.out.println("Order " + order.getId() + ":\n" + order);
            Map<Product, Integer> listOfProducts = OrderItemService.readProductsWithQuantitiesByOrderId(order.getId());
            System.out.println("Products: [");
            for(Map.Entry<Product, Integer> entry : listOfProducts.entrySet()){
                System.out.println(" * Product " + entry.getKey().getId() + " : " + entry.getKey() + ", Quantity: " + entry.getValue());
            }
            System.out.println("]");
        }
    }
}
