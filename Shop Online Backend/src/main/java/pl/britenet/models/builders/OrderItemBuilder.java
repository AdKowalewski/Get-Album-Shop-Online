package pl.britenet.models.builders;

import pl.britenet.models.OrderItem;

public class OrderItemBuilder extends BaseBuilder {

    private final OrderItem orderItem;

    public OrderItemBuilder() {
        this.orderItem = new OrderItem();
    }

    public OrderItemBuilder setId(int id){
        this.orderItem.setId(id);
        return this;
    }

    public OrderItemBuilder setOrder(int orderId){
        this.orderItem.setOrderId(orderId);
        return this;
    }

    public OrderItemBuilder setProduct(int productId){
        this.orderItem.setProductId(productId);
        return this;
    }

    public OrderItemBuilder setQuantity(int quantity){
        this.orderItem.setQuantity(quantity);
        return this;
    }

    @Override
    public OrderItem build() {
        return this.orderItem;
    }
}
