package pl.britenet.models.builders;

import pl.britenet.models.Client;
import pl.britenet.models.User;

public class UserBuilder extends BaseBuilder {

    private final User user;

    public UserBuilder(){
        this.user = new User();
    }

    public UserBuilder setId(int id) {
        this.user.setId(id);
        return this;
    }

    public UserBuilder setClient(Client client){
        this.user.setClient(client);
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.user.setEmail(email);
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.user.setPassword(password);
        return this;
    }

    @Override
    public User build(){
        return this.user;
    }
}
