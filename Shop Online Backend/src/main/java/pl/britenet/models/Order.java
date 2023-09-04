package pl.britenet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {

    private int id;
    private Client client;
    private Address address;
    private Map<Integer, Integer> products;
    private Timestamp created;
    private Timestamp modified;

    @Override
    public String toString() {
        return "User: " + client.getName() + " " + client.getSurname() + "\nAddress: " + address.toString() + "\nCreated: " + created.toString() +
                "\nModified: " + modified.toString();
    }
}
