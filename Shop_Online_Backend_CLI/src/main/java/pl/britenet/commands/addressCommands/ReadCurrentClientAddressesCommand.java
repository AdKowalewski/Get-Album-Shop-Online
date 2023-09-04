package pl.britenet.commands.addressCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Address;
import pl.britenet.models.Client;
import pl.britenet.services.AddressService;
import pl.britenet.services.ClientService;
import pl.britenet.services.UserService;

import java.util.List;

public class ReadCurrentClientAddressesCommand implements ICommand {

//    private final AddressService addressService;
//    private final UserService userService;
//    private final ClientService clientService;
//
//    public ReadCurrentClientAddressesCommand(){
//        this.addressService = new AddressService();
//        this.userService = new UserService();
//        this.clientService = new ClientService();
//    }

    @Override
    public void execute() {
        Client client = ClientService.getClientObjectById(UserService.getCurrentUser().getId());
        List<Address> clientAddresses = AddressService.readClientAddresses(client.getId());

        if(clientAddresses.size() == 0) {
            System.out.println(Colors.YELLOW.getColor() + "You have no addresses!" + Colors.RESET.getColor());
        } else {
            System.out.println("List of Your addresses:");
            for (int i = 0; i < clientAddresses.size(); i++) {
                System.out.print("Address " + clientAddresses.get(i).getId() + " : ");
                System.out.println(clientAddresses.get(i));
                System.out.println();
            }
        }
    }
}
