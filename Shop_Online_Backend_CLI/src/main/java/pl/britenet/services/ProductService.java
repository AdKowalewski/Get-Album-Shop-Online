package pl.britenet.services;

import pl.britenet.database.DatabaseService;
import pl.britenet.models.Product;
import pl.britenet.models.builders.ProductBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private static final DatabaseService databaseService = DatabaseService.getInstance();

    public static void createProduct(String artist, String title, String genre, String label, int year){
        databaseService.performDML(String.format(
                        "INSERT INTO product (artist, title, genre, label, release_year) VALUES ('%s', '%s', '%s', '%s', %d)",
                        artist, title, genre, label, year
                )
        );
    }

    public static List<Product> readAllProducts(){
        return databaseService.performSQL(
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
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static Product readProductByTitle(String title){
        return databaseService.performSQL(
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
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static List<Product> readProductsByArtist(String artist){
        return databaseService.performSQL(
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
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static List<Product> readProductsByGenre(String genre){
        return databaseService.performSQL(
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
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static Product getProductObjectById(int id) {
        return databaseService.performSQL(
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
                                    .build();
                        } else {
                            return null;
                        }
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static void updateProductOfGivenId(int id, String updatedArtist, String updatedTitle, String updatedGenre,
                                              String updatedLabel, String updatedYearStr){
        final var oldProduct = getProductObjectById(id);
        databaseService.performDML(String.format(
                "UPDATE product SET artist='%s', title='%s', genre='%s', label='%s', release_year=%d WHERE product_id=%d",
                updatedArtist.equals("") ? oldProduct.getArtist() : updatedArtist,
                updatedTitle.equals("") ? oldProduct.getTitle() : updatedTitle,
                updatedGenre.equals("") ? oldProduct.getGenre() : updatedGenre,
                updatedLabel.equals("") ? oldProduct.getLabel() : updatedLabel,
                updatedYearStr.equals("") ? oldProduct.getReleaseYear() : Integer.parseInt(updatedYearStr),
                id
        ));
    }

    public static void deleteProductById(int id){
        databaseService.performDML(String.format("DELETE FROM product WHERE product_id=%d", id));
    }

    //may be deleted in the future
    public static Product getLastInsert(){
        return databaseService.performSQL(
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