package pl.britenet.commands.orderCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.commands.addressCommands.CreateAddressCommand;
import pl.britenet.models.Address;
import pl.britenet.models.User;
import pl.britenet.services.AddressService;
import pl.britenet.services.ClientService;
import pl.britenet.services.OrderService;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class CreateOrderCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final OrderService orderService;
//    private final AddressService addressService;
//    private final UserService userService;
//    private final ClientService clientService;
//
//    public CreateOrderCommand(){
//        this.orderService = new OrderService();
//        this.addressService = new AddressService();
//        this.userService = new UserService();
//        this.clientService = new ClientService();
//    }

    @Override
    public void execute() {
        Map<Integer, Integer> orderProducts = new TreeMap<>();
        while(true){
            System.out.println(Colors.PURPLE.getColor() + "1. Add new product.");
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
                orderProducts.put(id, quantity);
            } else {
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
            }
            System.out.println();
        }
        if(orderProducts.size() == 0){
            System.out.println(Colors.YELLOW.getColor() + "Empty order will not be created!" + Colors.RESET.getColor());
        } else {
            Address address;
            List<Address> clientAddresses = AddressService.readClientAddresses(ClientService.getClientObjectById(UserService.getCurrentUser().getId()).getId());
            if(clientAddresses.size() == 0){
                ICommand command = new CreateAddressCommand();
                command.execute();
                address = AddressService.getLastInsert();
            } else {
                while(true) {
                    System.out.println(Colors.PURPLE.getColor() + "Choose from options below:");
                    System.out.println("1. Choose address from Your list.");
                    System.out.println("2. Provide new address.");
                    System.out.println(Colors.RESET.getColor());

                    System.out.print("Your choice: ");
                    int chosenOption;
                    try {
                        chosenOption = scanner.nextInt();
                    } catch (InputMismatchException ex) {
                        System.out.println();
                        System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                        address = null;
                        break;
                    }
                    System.out.println();

                    if(chosenOption == 1){
                        System.out.println("Your addresses:");
                        Set<Integer> addressIds = new HashSet<>();
                        for(int i = 0; i < clientAddresses.size(); i++){
                            System.out.println("Address " + clientAddresses.get(i).getId() + " : " + clientAddresses.get(i));
                            addressIds.add(clientAddresses.get(i).getId());
                        }
                        System.out.println();

                        System.out.print("I choose address: ");
                        int chosenAddress;
                        try {
                            chosenAddress = scanner.nextInt();
                        } catch (InputMismatchException ex) {
                            System.out.println();
                            System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                            address = null;
                            break;
                        }
                        System.out.println();

                        if(addressIds.contains(chosenAddress)) {
                            address = AddressService.getAddressObjectById(chosenAddress);
                            break;
                        } else {
                            System.out.println(Colors.RED.getColor() + "Invalid number!" + Colors.RESET.getColor());
                        }
                    } else if(chosenOption == 2){
                        ICommand command = new CreateAddressCommand();
                        command.execute();
                        address = AddressService.getLastInsert();
                        break;
                    } else {
                        System.out.println();
                        System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                    }
                }
            }

            assert address != null;
            OrderService.createOrder(ClientService.getClientObjectById(UserService.getCurrentUser().getId()), address, orderProducts);
            System.out.println(Colors.GREEN.getColor() + "Order created successfully!" + Colors.RESET.getColor());
            User curUser = UserService.getCurrentUser();
            curUser.setClient(ClientService.getClientObjectById(curUser.getId()));
            System.out.println(Colors.BLUE.getColor() + "For user: " + curUser + Colors.RESET.getColor());
        }
    }
}
