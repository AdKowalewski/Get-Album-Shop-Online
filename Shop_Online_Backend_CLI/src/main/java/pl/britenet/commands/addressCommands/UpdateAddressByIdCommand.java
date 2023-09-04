package pl.britenet.commands.addressCommands;

import lombok.NoArgsConstructor;
import pl.britenet.Colors;
import pl.britenet.commands.ICommand;
import pl.britenet.services.AddressService;
import pl.britenet.services.ScannerService;

import java.util.Scanner;

@NoArgsConstructor
public class UpdateAddressByIdCommand implements ICommand {

    private final Scanner scanner = ScannerService.getInstance();
    private Integer indexToUpdate;
//    private final AddressService addressService;
//
//    public UpdateAddressByIdCommand(){
//        this.addressService = new AddressService();
//    }

    public UpdateAddressByIdCommand(Integer indexToUpdate){
        this.indexToUpdate = indexToUpdate;
        //this.addressService = new AddressService();
    }

    @Override
    public void execute() {
        if(this.indexToUpdate == null){
            System.out.print("Type index of address You want to update: ");
            this.indexToUpdate = scanner.nextInt();
            System.out.println();
        }
        scanner.nextLine();
        System.out.print("Type new country: ");
        String updatedCountry = scanner.nextLine();
        System.out.print("Type new town: ");
        String updatedTown = scanner.nextLine();
        System.out.print("Type new street: ");
        String updatedStreet = scanner.nextLine();
        System.out.print("Type new street number: ");
        String updatedStreetNumberStr = scanner.nextLine();
        System.out.print("Type new local number: ");
        String updatedLocalNumberStr = scanner.nextLine();
        System.out.print("Type new postal code: ");
        String updatedPostalCode = scanner.nextLine();
        System.out.println();
        AddressService.updateAddressById(this.indexToUpdate, updatedCountry, updatedTown, updatedStreet, updatedStreetNumberStr, updatedLocalNumberStr, updatedPostalCode);
        System.out.println(Colors.GREEN.getColor() + "Address " + indexToUpdate + " updated successfully!" +
                Colors.RESET.getColor());
    }
}
