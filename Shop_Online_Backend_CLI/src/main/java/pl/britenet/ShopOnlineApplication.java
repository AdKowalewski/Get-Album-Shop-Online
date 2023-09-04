package pl.britenet;

import pl.britenet.commands.ICommand;
import pl.britenet.commands.addressCommands.AddressMainCommand;
import pl.britenet.commands.orderCommands.OrderMainCommand;
import pl.britenet.commands.productCommands.ProductMainCommand;
import pl.britenet.commands.userCommands.UserMainCommand;
import pl.britenet.services.ScannerService;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class ShopOnlineApplication {
    public static void main(String[] args) {

        Scanner scanner = ScannerService.getInstance();

        System.out.println();
        System.out.println("|\\        /|    |      |    /------\\    |   /------\\");
        System.out.println("| \\      / |    |      |    |           |   |");
        System.out.println("|  \\    /  |    |      |    \\______\\    |   |");
        System.out.println("|   \\  /   |    |      |           |    |   |");
        System.out.println("|    \\/    |    |      |           |    |   |");
        System.out.println("|          |    \\______/    \\______/    |   \\______/");
        System.out.println();
        System.out.println("/------\\    |      |    /------\\    |------\\");
        System.out.println("|           |      |    |      |    |      |");
        System.out.println("\\______\\    |______|    |      |    |      |");
        System.out.println("       |    |      |    |      |    |______/");
        System.out.println("       |    |      |    |      |    |");
        System.out.println("\\______/    |      |    \\______/    |");

        System.out.println();
        System.out.println(Colors.GREEN.getColor() + "Welcome to our Music Shop!" + Colors.RESET.getColor());
        System.out.println();

        boolean isWorking = true;

        Map<Integer, ICommand> commands = new HashMap<>();
        commands.put(1, new ProductMainCommand());
        commands.put(2, new UserMainCommand());
        commands.put(3, new AddressMainCommand());
        commands.put(4, new OrderMainCommand());

        while(isWorking) {
            System.out.println(Colors.PURPLE.getColor() + "Choose preferred category from below:");
            System.out.println("1. Products.");
            System.out.println("2. Users.");
            System.out.println("3. Addresses.");
            System.out.println("4. Orders.");
            System.out.println("0. Leave the shop.");
            System.out.println(Colors.RESET.getColor());

            System.out.print("Your choice: ");
            int chosenCategory;
            try{
                chosenCategory = scanner.nextInt();
            } catch (InputMismatchException ex){
                System.out.println();
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
                break;
            }

            System.out.println();

            if(chosenCategory == 0) {
                System.out.println(Colors.GREEN.getColor() + "Goodbye! We hope You enjoyed your visit in our shop!" +
                        Colors.RESET.getColor());
                isWorking = false;
            } else if(chosenCategory >= 1 && chosenCategory <= 4) {
                commands.get(chosenCategory).execute();
            } else {
                System.out.println(Colors.RED.getColor() + "Invalid input!" + Colors.RESET.getColor());
            }
        }
    }
}