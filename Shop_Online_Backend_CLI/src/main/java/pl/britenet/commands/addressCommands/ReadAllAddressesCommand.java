package pl.britenet.commands.addressCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Address;
import pl.britenet.services.AddressService;

import java.util.List;

public class ReadAllAddressesCommand implements ICommand {

//    private final AddressService addressService;
//
//    public ReadAllAddressesCommand(){
//        this.addressService = new AddressService();
//    }

    @Override
    public void execute() {
        List<Address> addresses = AddressService.readAllAddresses();
        if (addresses.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "There are no any address!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of addresses:");
            for (int i = 0; i < addresses.size(); i++) {
                System.out.print("Address " + addresses.get(i).getId() + " : ");
                System.out.println(addresses.get(i));
            }
        }
    }
}
