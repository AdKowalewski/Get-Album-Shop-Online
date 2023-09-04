package pl.britenet.commands.addressCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.AddressService;
import pl.britenet.services.ClientService;
import pl.britenet.services.ScannerService;
import pl.britenet.services.UserService;

import java.util.Scanner;

public class CreateAddressCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final AddressService addressService;
//    private final UserService userService;
//    private final ClientService clientService;
//
//    public CreateAddressCommand(){
//        this.addressService = new AddressService();
//        this.userService = new UserService();
//        this.clientService = new ClientService();
//    }

    @Override
    public void execute() {
        Integer streetNumber;
        Integer localNumber;
        System.out.print("Type country: ");
        String country = scanner.nextLine();
        System.out.print("Type town: ");
        String town = scanner.nextLine();
        System.out.print("Type street: ");
        String street = scanner.nextLine();
        System.out.print("Type street number: ");
        String streetNumberStr = scanner.nextLine();
        System.out.print("Type local number: ");
        String localNumberStr = scanner.nextLine();
        System.out.print("Type postal code: ");
        String postalCode = scanner.nextLine();
        System.out.println();
        if(country.equals("") || town.equals("") || street.equals("") || streetNumberStr.equals("") || postalCode.equals("")){
            System.out.println(Colors.RED.getColor() + "All fields except local number must be filled!" + Colors.RESET.getColor());
        } else if (localNumberStr.equals("")) {
            streetNumber = Integer.parseInt(streetNumberStr);
            AddressService.createAddress(country, town, street, streetNumber, null, postalCode, ClientService.getClientObjectById(UserService.getCurrentUser().getId()).getId());
            System.out.println(Colors.GREEN.getColor() + "Address created successfully!" + Colors.RESET.getColor());
        } else {
            streetNumber = Integer.parseInt(streetNumberStr);
            localNumber = Integer.parseInt(localNumberStr);
            AddressService.createAddress(country, town, street, streetNumber, localNumber, postalCode, ClientService.getClientObjectById(UserService.getCurrentUser().getId()).getId());
            System.out.println(Colors.GREEN.getColor() + "Address created successfully!" + Colors.RESET.getColor());
        }
    }
}
