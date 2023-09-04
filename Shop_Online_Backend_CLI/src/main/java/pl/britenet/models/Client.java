package pl.britenet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Client {

    private int id;
    private String name;
    private String surname;

    @Override
    public String toString() {
        return "Name: " + name + " Surname: " + surname;
    }
}
