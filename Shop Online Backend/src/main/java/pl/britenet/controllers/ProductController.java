package pl.britenet.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.britenet.authorization.AuthorizationController;
import pl.britenet.exceptions.ResourceNotFoundException;
import pl.britenet.models.Product;
import pl.britenet.services.ProductService;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin("null")
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    private final UserService userService;

    private final AuthorizationController authorizationController;

    public ProductController(ProductService productService, UserService userService, AuthorizationController authorizationController) {
        this.productService = productService;
        this.userService = userService;
        this.authorizationController = authorizationController;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void create(@RequestBody Product product, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            this.productService.createProduct(product.getArtist(), product.getTitle(), product.getGenre(), product.getLabel(), product.getReleaseYear(), product.getPrice());
            //return this.productService.getLastInsert();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public List<Product> readAll(){
        return this.productService.readAllProducts();
    }

    @GetMapping("/title/{title}")
    public Optional<Product> readByTitle(@PathVariable String title){
        return Optional.ofNullable(Optional.ofNullable(this.productService.readProductByTitle(title)).orElseThrow(() -> new ResourceNotFoundException("Product not found.")));
    }

    @GetMapping("/artist/{artist}")
    public List<Product> readByArtist(@PathVariable String artist){
        return this.productService.readProductsByArtist(artist);
    }

    @GetMapping("/titleOrArtist/{titleOrArtist}")
    public List<Product> readByTitleOrArtist(@PathVariable String titleOrArtist){
        return this.productService.readProductsByTitleOrArtist(titleOrArtist);
    }

    @GetMapping("/genre/{genre}")
    public List<Product> readByGenre(@PathVariable String genre){
        return this.productService.readProductsByGenre(genre);
    }

    @GetMapping("/{id}")
    public Optional<Product> getById(@PathVariable int id){
        return Optional.ofNullable(Optional.ofNullable(this.productService.getProductObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found.")));
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Product updateById(@RequestBody Product product, @PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            try {
                this.productService.updateProductOfGivenId(
                        id,
                        product.getArtist(),
                        product.getTitle(),
                        product.getGenre(),
                        product.getLabel(),
                        product.getReleaseYear().toString(),
                        product.getPrice().toString());
            } catch (Exception e) {
                throw new ResourceNotFoundException("Product " + id + " not found.");
            }
            return this.productService.getProductObjectById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.productService.getProductObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Product " + id + " not found."));
            this.productService.deleteProductById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
