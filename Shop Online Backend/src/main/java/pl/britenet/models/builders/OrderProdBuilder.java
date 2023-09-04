package pl.britenet.models.builders;

import pl.britenet.models.OrderProd;

public class OrderProdBuilder extends BaseBuilder {

    private final OrderProd orderProd;

    public OrderProdBuilder(){
        this.orderProd = new OrderProd();
    }

    public OrderProdBuilder setId(int id) {
        this.orderProd.setId(id);
        return this;
    }

    public OrderProdBuilder setArtist(String artist) {
        this.orderProd.setArtist(artist);
        return this;
    }

    public OrderProdBuilder setTitle(String title) {
        this.orderProd.setTitle(title);
        return this;
    }

    public OrderProdBuilder setGenre(String genre) {
        this.orderProd.setGenre(genre);
        return this;
    }

    public OrderProdBuilder setLabel(String label) {
        this.orderProd.setLabel(label);
        return this;
    }

    public OrderProdBuilder setReleaseYear(Integer releaseYear) {
        this.orderProd.setReleaseYear(releaseYear);
        return this;
    }

    public OrderProdBuilder setPrice(Integer price){
        this.orderProd.setPrice(price);
        return this;
    }

    public OrderProdBuilder setQuantity(Integer quantity){
        this.orderProd.setQuantity(quantity);
        return this;
    }

    @Override
    public OrderProd build(){
        return this.orderProd;
    }
}
