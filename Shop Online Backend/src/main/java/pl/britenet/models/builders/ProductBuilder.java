package pl.britenet.models.builders;

import pl.britenet.models.Product;

public class ProductBuilder extends BaseBuilder {

    private final Product product;

    public ProductBuilder(){
        this.product = new Product();
    }

    public ProductBuilder setId(int id) {
        this.product.setId(id);
        return this;
    }

    public ProductBuilder setArtist(String artist) {
        this.product.setArtist(artist);
        return this;
    }

    public ProductBuilder setTitle(String title) {
        this.product.setTitle(title);
        return this;
    }

    public ProductBuilder setGenre(String genre) {
        this.product.setGenre(genre);
        return this;
    }

    public ProductBuilder setLabel(String label) {
        this.product.setLabel(label);
        return this;
    }

    public ProductBuilder setReleaseYear(Integer releaseYear) {
        this.product.setReleaseYear(releaseYear);
        return this;
    }

    public ProductBuilder setPrice(Integer price){
        this.product.setPrice(price);
        return this;
    }

    @Override
    public Product build(){
        return this.product;
    }
}
