package pl.britenet.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {

    private int id;
    private String artist;
    private String title;
    private String genre;
    private String label;
    private Integer releaseYear;

    @Override
    public String toString() {
        return "Title: " + title + ", Artist: " + artist + ", Genre: " + genre + ", Label: " +
                label + ", Released in: " + releaseYear;
    }
}
