package pl.britenet.commands.orderCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Client;
import pl.britenet.models.Order;
import pl.britenet.models.Product;
import pl.britenet.services.ClientService;
import pl.britenet.services.OrderItemService;
import pl.britenet.services.OrderService;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Map;

public class ReadCurrentClientOrdersCommand implements ICommand {

//    private final OrderService orderService;
//    private final OrderItemService orderItemService;
//    private final UserService userService;
//    private final ClientService clientService;
//
//    public ReadCurrentClientOrdersCommand(){
//        this.orderService = new OrderService();
//        this.orderItemService = new OrderItemService();
//        this.userService = new UserService();
//        this.clientService = new ClientService();
//    }

    @Override
    public void execute() {
        Client currentClient = ClientService.getClientObjectById(UserService.getCurrentUser().getId());

        if(currentClient == null){
            System.out.println(Colors.YELLOW.getColor() + "There is no such client!" + Colors.RESET.getColor());
        } else {
            List<Order> userOrders = OrderService.readCurrentClientOrders(currentClient.getId());

            if (userOrders.size() == 0) {
                System.out.println(Colors.YELLOW.getColor() + "You have no orders!" + Colors.RESET.getColor());
            } else {
                System.out.println("List of Your orders:");
                for (int i = 0; i < userOrders.size(); i++) {
                    System.out.println("Order " + userOrders.get(i).getId() + ":");
                    System.out.println(userOrders.get(i));
                    Map<Product, Integer> listOfProducts = OrderItemService.readProductsWithQuantitiesByOrderId(userOrders.get(i).getId());
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
}
