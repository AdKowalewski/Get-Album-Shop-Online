package pl.britenet.commands.orderCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Order;
import pl.britenet.services.OrderService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class DeleteOrderByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final OrderService orderService;
//
//    public DeleteOrderByIdCommand(){
//        this.orderService = new OrderService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of order You want to delete: ");
        int indexToDelete = scanner.nextInt();
        Order order = OrderService.getOrderObjectById(indexToDelete);
        if(order != null){
            OrderService.deleteOrderById(indexToDelete);
            System.out.println(Colors.GREEN.getColor() + "Order " + indexToDelete + " deleted successfully!" +
                    Colors.RESET.getColor());
        } else {
            System.out.println(Colors.YELLOW.getColor() + "There is no such order!" + Colors.RESET.getColor());
        }
    }
}
