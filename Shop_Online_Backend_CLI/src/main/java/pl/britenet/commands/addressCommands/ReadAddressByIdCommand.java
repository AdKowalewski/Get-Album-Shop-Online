package pl.britenet.commands.addressCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Address;
import pl.britenet.services.AddressService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class ReadAddressByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final AddressService addressService;
//
//    public ReadAddressByIdCommand(){
//        this.addressService = new AddressService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of address: ");
        int index = scanner.nextInt();

        Address address = AddressService.getAddressObjectById(index);

        if(address == null) {
            System.out.println(Colors.YELLOW.getColor() + "There is no such address!" + Colors.RESET.getColor());
        } else {
            System.out.println("Address " + address.getId() + " : " + address);
        }
    }
}
