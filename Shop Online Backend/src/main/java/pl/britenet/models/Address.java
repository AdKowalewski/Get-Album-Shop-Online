package pl.britenet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Address {

    private int id;
    private String country;
    private String town;
    private String street;
    private Integer streetNumber;
    private Integer localNumber;
    private String postalCode;
    private int clientId;

    @Override
    public String toString() {
        if(this.localNumber != 0) {
            return "Address: " + country + ", Town: " + town + ", Street: " + street + " " + streetNumber + "/" +
                    localNumber + ", " + postalCode + ", Client: " + clientId;
        } else {
            return "Address: " + country + ", Town: " + town + ", Street: " + street + " " + streetNumber + ", " + postalCode + ", Client: " + clientId;
        }
    }
}
