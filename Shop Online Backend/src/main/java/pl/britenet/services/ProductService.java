package pl.britenet.services;

import org.springframework.stereotype.Service;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.Product;
import pl.britenet.models.builders.ProductBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final DatabaseService databaseService;

    public ProductService(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    public void createProduct(String artist, String title, String genre, String label, int year, int price){
        this.databaseService.performDML(String.format(
                        "INSERT INTO product (artist, title, genre, label, release_year, price) VALUES ('%s', '%s', '%s', '%s', %d, %d)",
                        artist, title, genre, label, year, price
                )
        );
    }

    public List<Product> readAllProducts(){
        return this.databaseService.performSQL(
                "SELECT * FROM product",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Product>();
                        while (resultSet.next()) {
                            items.add(new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public Product readProductByTitle(String title){
        return this.databaseService.performSQL(
                "SELECT * FROM product WHERE title LIKE \"%" + title + "%\"",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<Product> readProductsByArtist(String artist){
        return this.databaseService.performSQL(
                "SELECT * FROM product WHERE artist LIKE \"%" + artist + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Product>();
                        while (resultSet.next()) {
                            items.add(new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<Product> readProductsByTitleOrArtist(String input){
        return this.databaseService.performSQL(
                "SELECT * FROM product WHERE artist LIKE \"%" + input + "%\" OR title LIKE \"%" + input + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Product>();
                        while (resultSet.next()) {
                            items.add(new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<Product> readProductsByGenre(String genre){
        return this.databaseService.performSQL(
                "SELECT * FROM product WHERE genre LIKE \"%" + genre + "%\"",
                resultSet -> {
                    try {
                        final var items = new ArrayList<Product>();
                        while (resultSet.next()) {
                            items.add(new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public Product getProductObjectById(int id) {
        return this.databaseService.performSQL(
                "SELECT * FROM product WHERE product_id = " + id,
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public void updateProductOfGivenId(int id, String updatedArtist, String updatedTitle, String updatedGenre,
                                              String updatedLabel, String updatedYearStr, String updatedPrice){
        final var oldProduct = getProductObjectById(id);
        this.databaseService.performDML(String.format(
                "UPDATE product SET artist='%s', title='%s', genre='%s', label='%s', release_year=%d, price=%d WHERE product_id=%d",
                updatedArtist.equals("") ? oldProduct.getArtist() : updatedArtist,
                updatedTitle.equals("") ? oldProduct.getTitle() : updatedTitle,
                updatedGenre.equals("") ? oldProduct.getGenre() : updatedGenre,
                updatedLabel.equals("") ? oldProduct.getLabel() : updatedLabel,
                updatedYearStr.equals("") ? oldProduct.getReleaseYear() : Integer.parseInt(updatedYearStr),
                updatedPrice.equals("") ? oldProduct.getPrice() : Integer.parseInt(updatedPrice),
                id
        ));
    }

    public void deleteProductById(int id){
        this.databaseService.performDML(String.format("DELETE FROM product WHERE product_id=%d", id));
    }

    //may be deleted in the future
    public Product getLastInsert(){
        return this.databaseService.performSQL(
                "SELECT * FROM product WHERE product_id=LAST_INSERT_ID()",
                resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }
}