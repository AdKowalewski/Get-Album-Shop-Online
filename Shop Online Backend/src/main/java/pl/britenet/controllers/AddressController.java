package pl.britenet.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.britenet.authorization.AuthorizationController;
import pl.britenet.exceptions.ResourceNotFoundException;
import pl.britenet.models.Address;
import pl.britenet.services.AddressService;
import pl.britenet.services.ClientService;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/address")
public class AddressController {

    private final AddressService addressService;
    private final ClientService clientService;

    private final UserService userService;
    private final AuthorizationController authorizationController;

    public AddressController(AddressService addressService, ClientService clientService, UserService userService, AuthorizationController authorizationController) {
        this.addressService = addressService;
        this.clientService = clientService;
        this.userService = userService;
        this.authorizationController = authorizationController;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Address create(@RequestBody Address address, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            this.addressService.createAddress(
                    address.getCountry(),
                    address.getTown(),
                    address.getStreet(),
                    address.getStreetNumber(),
                    address.getLocalNumber(),
                    address.getPostalCode(),
                    address.getClientId());
            return this.addressService.getLastInsert();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public List<Address> readAll(){
        return this.addressService.readAllAddresses();
    }

    @GetMapping("/{id}")
    public Optional<Address> getById(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            return Optional.ofNullable(Optional.ofNullable(this.addressService.getAddressObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Address " + id + " not found.")));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/client/{id}")
    public List<Address> readByClient(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.clientService.getClientObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Client " + id + " not found."));
            return this.addressService.readClientAddresses(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Address updateById(@RequestBody Address address, @PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            try {
                this.addressService.updateAddressById(
                        id,
                        address.getCountry(),
                        address.getTown(),
                        address.getStreet(),
                        address.getStreetNumber().toString(),
                        address.getLocalNumber().toString(),
                        address.getPostalCode()
                );
            } catch (Exception e) {
                throw new ResourceNotFoundException("Address " + id + " not found.");
            }
            return this.addressService.getAddressObjectById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.addressService.getAddressObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Address " + id + " not found."));
            this.addressService.deleteAddressById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
    
    @DeleteMapping("/disable/{id}")
    public void disableById(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)){
            this.addressService.disableAddressById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/client/{id}")
    public void deleteByClientId(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.clientService.getClientObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Client " + id + " not found."));
            this.addressService.deleteAddressesByClientId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
