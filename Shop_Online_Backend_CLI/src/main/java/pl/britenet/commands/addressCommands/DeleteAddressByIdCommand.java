package pl.britenet.commands.addressCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Address;
import pl.britenet.services.AddressService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class DeleteAddressByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final AddressService addressService;
//
//    public DeleteAddressByIdCommand(){
//        this.addressService = new AddressService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of address You want to delete: ");
        int indexToDelete = scanner.nextInt();
        Address address = AddressService.getAddressObjectById(indexToDelete);
        if(address != null){
            AddressService.deleteAddressById(indexToDelete);
            System.out.println(Colors.GREEN.getColor() + "Address " + indexToDelete + " deleted successfully!" +
                    Colors.RESET.getColor());
        } else {
            System.out.println(Colors.YELLOW.getColor() + "There is no such address!" + Colors.RESET.getColor());
        }
    }
}
