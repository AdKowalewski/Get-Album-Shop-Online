package pl.britenet.commands.addressCommands;

import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.models.Client;
import pl.britenet.services.AddressService;
import pl.britenet.services.ClientService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

public class DeleteAddressesByClientIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();

//    private final AddressService addressService;
//    private final ClientService clientService;
//
//    public DeleteAddressesByClientIdCommand(){
//        this.addressService = new AddressService();
//        this.clientService = new ClientService();
//    }

    @Override
    public void execute() {
        System.out.print("Type index of client whose addresses You want to delete: ");
        int index = scanner.nextInt();
        Client client = ClientService.getClientObjectById(index);
        if(client != null){
            AddressService.deleteAddressesByClientId(index);
            System.out.println(Colors.GREEN.getColor() + "Addresses of client: " + client.getName() + " " +
                    client.getSurname() + " deleted successfully!" +
                    Colors.RESET.getColor());
        } else {
            System.out.println(Colors.YELLOW.getColor() + "There is no such client!" + Colors.RESET.getColor());
        }
    }
}
