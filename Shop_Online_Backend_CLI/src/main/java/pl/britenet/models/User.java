package pl.britenet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    private int id;
    private String email;
    private String password;
    private Client client;

    @Override
    public String toString() {
        return "Name: " + client.getName() + ", Surname: " + client.getSurname() + ", Email: " +
                email + ", Password: " + password;
    }
}