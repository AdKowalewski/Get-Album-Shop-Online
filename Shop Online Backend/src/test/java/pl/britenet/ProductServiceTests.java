package pl.britenet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class ProductServiceTests {

    DatabaseService databaseService = new DatabaseService();

    @Test
    void testIfProductCreateWorksProperly(){
        ProductService productService = new ProductService(databaseService);

        productService.createProduct("example artist", "example title", "example genre", "example label", 2022, 60);
        Optional<Product> product = Optional.ofNullable(productService.getLastInsert());

        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals("example artist", product.get().getArtist());
        Assertions.assertEquals("example title", product.get().getTitle());
        Assertions.assertEquals("example genre", product.get().getGenre());
        Assertions.assertEquals("example label", product.get().getLabel());
        Assertions.assertEquals(2022, product.get().getReleaseYear());
        Assertions.assertEquals(60, product.get().getPrice());
    }

    @Test
    void testIfListOfAllProductsIsReadProperly(){
        ProductService productService = new ProductService(databaseService);

        Optional<List<Product>> products = Optional.ofNullable(productService.readAllProducts());

        Assertions.assertTrue(products.isPresent());
    }

    @Test
    void testIfThereIsProductByGivenTitle(){
        ProductService productService = new ProductService(databaseService);

        Optional<Product> product = Optional.ofNullable(productService.readProductByTitle("Master of Puppets"));

        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals("Master of Puppets", product.get().getTitle());
    }

    @Test
    void testIfThereIsNotProductByGivenTitle(){
        ProductService productService = new ProductService(databaseService);

        Optional<Product> product = Optional.ofNullable(productService.readProductByTitle("gdfgdfgdf"));

        Assertions.assertFalse(product.isPresent());
    }

    @Test
    void testIfThereAreProductsByGivenArtist(){
        ProductService productService = new ProductService(databaseService);

        Optional<List<Product>> products = Optional.ofNullable(productService.readProductsByArtist("Metallica"));

        Assertions.assertTrue(products.isPresent());
        Assertions.assertEquals("Metallica", products.get().get(0).getArtist());
    }

    @Test
    void testIfThereAreNotProductsByGivenArtist(){
        ProductService productService = new ProductService(databaseService);

        Optional<List<Product>> products = Optional.ofNullable(productService.readProductsByArtist("hdfhhghhs"));

        Assertions.assertFalse(products.isPresent());
    }

    @Test
    void testIfThereAreProductsByGivenGenre(){
        ProductService productService = new ProductService(databaseService);

        Optional<List<Product>> products = Optional.ofNullable(productService.readProductsByGenre("thrash metal"));

        Assertions.assertTrue(products.isPresent());
        Assertions.assertEquals("thrash metal", products.get().get(0).getGenre());
    }

    @Test
    void testIfThereAreNotProductsByGivenGenre(){
        ProductService productService = new ProductService(databaseService);

        Optional<List<Product>> products = Optional.ofNullable(productService.readProductsByGenre("punk"));

        Assertions.assertFalse(products.isPresent());
    }

    @Test
    void testIfThereIsProductByGivenId(){
        ProductService productService = new ProductService(databaseService);

        Optional<Product> product = Optional.ofNullable(productService.getProductObjectById(3));

        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals("Metallica", product.get().getArtist());
        Assertions.assertEquals("Ride the Lightning", product.get().getTitle());
        Assertions.assertEquals("thrash metal", product.get().getGenre());
        Assertions.assertEquals("Megaforce", product.get().getLabel());
        Assertions.assertEquals(1984, product.get().getReleaseYear());
        Assertions.assertEquals(60, product.get().getPrice());
    }

    @Test
    void testIfThereIsNotProductByGivenId(){
        ProductService productService = new ProductService(databaseService);

        Optional<Product> product = Optional.ofNullable(productService.getProductObjectById(2137));

        Assertions.assertFalse(product.isPresent());
    }

    @Test
    void testIfProductUpdateWorksProperly(){
        ProductService productService = new ProductService(databaseService);

        productService.updateProductOfGivenId(2, "", "", "", "Disco Polo", "", "");
        Optional<Product> product = Optional.ofNullable(productService.getProductObjectById(2));

        Assertions.assertTrue(product.isPresent());
        Assertions.assertEquals("Zenek Martyniuk", product.get().getArtist());
        Assertions.assertEquals("Oczy Zielone", product.get().getTitle());
        Assertions.assertEquals("disco polo", product.get().getGenre());
        Assertions.assertEquals("Disco Polo", product.get().getLabel());
        Assertions.assertEquals(2014, product.get().getReleaseYear());
        Assertions.assertEquals(60, product.get().getPrice());
    }

    @Test
    void testIfProductDeleteWorksProperly(){
        ProductService productService = new ProductService(databaseService);

        productService.deleteProductById(18);
        Optional<Product> product = Optional.ofNullable(productService.getProductObjectById(18));

        Assertions.assertFalse(product.isPresent());
    }
}
