package pl.britenet.models.builders;

import pl.britenet.models.Address;
import pl.britenet.models.Client;
import pl.britenet.models.Order;

import java.sql.Timestamp;
import java.util.Map;

public class OrderBuilder extends BaseBuilder {

    private final Order order;

    public OrderBuilder(){
        this.order = new Order();
    }

    public OrderBuilder setId(int id) {
        this.order.setId(id);
        return this;
    }

    public OrderBuilder setClient(Client client) {
        this.order.setClient(client);
        return this;
    }

    public OrderBuilder setAddress(Address address){
        this.order.setAddress(address);
        return this;
    }

    public OrderBuilder setProducts(Map<Integer, Integer> products) {
        this.order.setProducts(products);
        return this;
    }

    public OrderBuilder setCreated(Timestamp created) {
        this.order.setCreated(created);
        return this;
    }

    public OrderBuilder setModified(Timestamp modified) {
        this.order.setModified(modified);
        return this;
    }

    @Override
    public Order build(){
        return this.order;
    }
}
