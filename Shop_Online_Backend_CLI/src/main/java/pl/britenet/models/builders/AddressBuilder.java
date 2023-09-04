package pl.britenet.models.builders;

import pl.britenet.models.Address;
import pl.britenet.models.Client;

public class AddressBuilder extends BaseBuilder {

    private final Address address;

    public AddressBuilder() {
        this.address = new Address();
    }

    public AddressBuilder setId(int id){
        this.address.setId(id);
        return this;
    }

    public AddressBuilder setCountry(String country){
        this.address.setCountry(country);
        return this;
    }

    public AddressBuilder setTown(String town){
        this.address.setTown(town);
        return this;
    }

    public AddressBuilder setStreet(String street){
        this.address.setStreet(street);
        return this;
    }

    public AddressBuilder setStreetNumber(Integer streetNumber){
        this.address.setStreetNumber(streetNumber);
        return this;
    }

    public AddressBuilder setLocalNumber(Integer localNumber){
        this.address.setLocalNumber(localNumber);
        return this;
    }

    public AddressBuilder setPostalCode(String postalCode){
        this.address.setPostalCode(postalCode);
        return this;
    }

    public AddressBuilder setClientId(int clientId){
        this.address.setClientId(clientId);
        return this;
    }

    @Override
    public Address build() {
        return this.address;
    }
}
