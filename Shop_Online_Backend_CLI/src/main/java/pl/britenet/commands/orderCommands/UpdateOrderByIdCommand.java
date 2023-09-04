package pl.britenet.commands.orderCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.commands.addressCommands.UpdateAddressByIdCommand;
import pl.britenet.models.Address;
import pl.britenet.models.Order;
import pl.britenet.models.Product;
import pl.britenet.services.OrderItemService;
import pl.britenet.services.OrderService;
import pl.britenet.services.ScannerService;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class UpdateOrderByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final OrderService orderService;
//    private final OrderItemService orderItemService;
//
//    public UpdateOrderByIdCommand(){
//        this.orderService = new OrderService();
//        this.orderItemService = new OrderItemService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of order You want to update: ");
        int indexToUpdate = scanner.nextInt();
        System.out.println();

        Order order = OrderService.getOrderObjectById(indexToUpdate);

        if(order != null){
            Map<Integer, Integer> orderProductsWithQuantities = OrderItemService.readOrderItemsByOrderId(indexToUpdate);
            List<Product> orderProducts = OrderItemService.readProductsByOrderId(indexToUpdate);
            Set<Integer> productIds = new HashSet<>();

            while(true){
                Address currentAddress = order.getAddress();
                System.out.println("Your current address is: " + currentAddress);
                System.out.println(Colors.PURPLE.getColor() + "Do You want to update Your address?:");
                System.out.println("1. Yes, I do.");
                System.out.println("0. No, I don't.");
                System.out.println(Colors.RESET.getColor());

                System.out.print("Your choice: ");
                int chosenOption;
                try {
                    chosenOption = scanner.nextInt();
                } catch (InputMismatchException ex) {
                    System.out.println();
                    System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                    break;
                }
                System.out.println();

                if(chosenOption == 0){
                    break;
                } else if(chosenOption == 1){
                    ICommand command = new UpdateAddressByIdCommand(currentAddress.getId());
                    command.execute();
                } else {
                    System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                }
                System.out.println();
            }

            System.out.println("List of order products:");
            for(int i = 0; i < orderProducts.size(); i++){
                System.out.println("Product " + orderProducts.get(i).getId() + " : " + orderProducts.get(i) + ", Quantity: " + OrderItemService.getProductQuantityByOrderId(indexToUpdate, orderProducts.get(i).getId()));
                productIds.add(orderProducts.get(i).getId());
            }

            while(true) {
                System.out.println(Colors.PURPLE.getColor() + "Choose one option from below:");
                System.out.println("1. Add next product.");
                System.out.println("2. Update products' quantity by id in the list.");
                System.out.println("3. Remove product by id in the list.");
                System.out.println("0. Cancel.");
                System.out.println(Colors.RESET.getColor());

                System.out.print("Your choice: ");
                int chosenOption;
                try {
                    chosenOption = scanner.nextInt();
                } catch (InputMismatchException ex) {
                    System.out.println();
                    System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                    break;
                }
                System.out.println();

                if(chosenOption == 0){
                    break;
                } else if(chosenOption == 1){
                    System.out.print("Type product id: ");
                    int id = scanner.nextInt();
                    System.out.print("Type quantity of this product: ");
                    int quantity = scanner.nextInt();
                    if(productIds.contains(id)) {
                        //OrderItemService.createOrderItem(indexToUpdate, id, quantity);
                        orderProductsWithQuantities.put(id, quantity);
                        System.out.println(Colors.GREEN.getColor() + "Product " + id + " added to Your order successfully!" +
                                Colors.RESET.getColor());
                    } else {
                        System.out.println(Colors.RED.getColor() + "Invalid number!" + Colors.RESET.getColor());
                    }
                } else if(chosenOption == 2){
                    System.out.print("Type product id: ");
                    int id = scanner.nextInt();
                    System.out.print("Type quantity of this product: ");
                    int quantity = scanner.nextInt();
                    if(productIds.contains(id) && quantity != 0) {
                        //OrderItemService.updateOrderItemQuantityByOrderIdAndProductId(indexToUpdate, id, quantity);
                        orderProductsWithQuantities.put(id, quantity);
                        System.out.println(Colors.GREEN.getColor() + "Quantity of item " + id + " from order " + indexToUpdate + " updated to " + quantity + " successfully!" +
                                Colors.RESET.getColor());
                    } else if(productIds.contains(id) && quantity == 0){
                        System.out.println(Colors.GREEN.getColor() + "Item " + id + " from order " + indexToUpdate + " with 0 quantity will be deleted!" +
                                Colors.RESET.getColor());
                        //OrderItemService.deleteOrderItemByOrderIdAndItemId(indexToUpdate, id);
                        orderProductsWithQuantities.remove(id);
                        if(orderProductsWithQuantities.isEmpty()){
                            System.out.println(Colors.YELLOW.getColor() + "Order is now empty and will be deleted!" +
                                    Colors.RESET.getColor());
                            OrderService.deleteOrderById(indexToUpdate);
                            order = null;
                            break;
                        }
                    } else {
                        System.out.println(Colors.RED.getColor() + "Invalid number!" + Colors.RESET.getColor());
                    }
                }else if(chosenOption == 3){
                    System.out.print("Type index of product You want to delete: ");
                    int indexToDelete = scanner.nextInt();
                    Product product = orderProducts.get(indexToDelete);
                    if(product != null){
                        //OrderItemService.deleteOrderItemByOrderIdAndItemId(indexToUpdate, indexToDelete);
                        orderProductsWithQuantities.remove(indexToDelete);
                        System.out.println(Colors.GREEN.getColor() + "Product " + indexToDelete +
                                " deleted successfully from Your order!" + Colors.RESET.getColor());
                    } else {
                        System.out.println(Colors.YELLOW.getColor() + "There is no such product!" +
                                Colors.RESET.getColor());
                    }
                    if(orderProductsWithQuantities.isEmpty()){
                        System.out.println(Colors.YELLOW.getColor() + "Order is now empty and will be deleted!" +
                                Colors.RESET.getColor());
                        OrderService.deleteOrderById(indexToUpdate);
                        break;
                    }
                } else {
                    System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                }
                System.out.println();
            }

            if(order != null) {
                OrderService.updateOrderById(indexToUpdate, orderProductsWithQuantities, order.getAddress());
                System.out.println(Colors.GREEN.getColor() + "Order " + order.getId() + " updated successfully!" +
                        Colors.RESET.getColor());
            } else {
                System.out.println(Colors.YELLOW.getColor() + "Order " + indexToUpdate + " already deleted!" + Colors.RESET.getColor());
            }

        } else {
            System.out.println(Colors.YELLOW.getColor() + "There is no such order!" + Colors.RESET.getColor());
        }
    }
}
