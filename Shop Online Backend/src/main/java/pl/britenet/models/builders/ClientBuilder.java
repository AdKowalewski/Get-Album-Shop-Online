package pl.britenet.models.builders;

import pl.britenet.models.Client;

public class ClientBuilder extends BaseBuilder {

    private final Client client;

    public ClientBuilder() {
        this.client = new Client();
    }

    public ClientBuilder setId(int id){
        this.client.setId(id);
        return this;
    }

    public ClientBuilder setName(String name){
        this.client.setName(name);
        return this;
    }

    public ClientBuilder setSurname(String surname){
        this.client.setSurname(surname);
        return this;
    }

    @Override
    public Client build() {
        return this.client;
    }
}
