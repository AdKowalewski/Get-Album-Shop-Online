package pl.britenet.services;

import org.springframework.stereotype.Service;
import pl.britenet.database.DatabaseService;
import pl.britenet.models.OrderItem;
import pl.britenet.models.OrderProd;
import pl.britenet.models.Product;
import pl.britenet.models.builders.OrderItemBuilder;
import pl.britenet.models.builders.OrderProdBuilder;
import pl.britenet.models.builders.ProductBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderItemService {

    private final DatabaseService databaseService;

    public OrderItemService(DatabaseService databaseService){
        this.databaseService = databaseService;
    }

    public void createOrderItem(Integer order, Integer product, int quantity){
        this.databaseService.performDML(String.format(
                        "INSERT INTO order_item (order_id, product_id, quantity) VALUES (%d, %d, %d)",
                        order, product, quantity
                )
        );
    }

    //may be deleted in the future
    public List<OrderItem> readAllOrderItems(){
        return this.databaseService.performSQL(
                "SELECT * FROM order_item",
                resultSet -> {
                    try {
                        final var items = new ArrayList<OrderItem>();
                        while (resultSet.next()) {
                            items.add(new OrderItemBuilder()
                                    .setId(resultSet.getInt("order_item_id"))
                                    .setOrder(resultSet.getInt("order_id"))
                                    .setProduct(resultSet.getInt("product_id"))
                                    .setQuantity(resultSet.getInt("quantity"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public Map<Integer, Integer> readOrderItemsByOrderId(int id){
        return this.databaseService.performSQL(
                "SELECT product_id, quantity FROM order_item WHERE order_id=" + id,
                resultSet -> {
                    try {
                        final var items = new HashMap<Integer, Integer>();
                        while (resultSet.next()) {
                            items.put(resultSet.getInt("product_id"), resultSet.getInt("quantity"));
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    //may be deleted in the future
    public Integer getProductQuantityByOrderId(int order, int product){
        return this.databaseService.performSQL(
             "SELECT quantity FROM order_item WHERE order_id=" + order + " AND product_id=" + product,
             resultSet -> {
                 try {
                     if(resultSet.next()){
                        return resultSet.getInt("quantity");
                     } else {
                         return null;
                     }
                 } catch (SQLException e) {
                     throw new IllegalStateException(e);
                 }
             }).get();
    }

    public List<Product> readProductsByOrderId(int id){
        return this.databaseService.performSQL(
                "SELECT * FROM product p JOIN order_item i ON i.product_id=p.product_id WHERE i.order_id=" + id,
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

    public List<OrderProd> readProductsWithQuantitiesByOrderId(int id){
        return this.databaseService.performSQL(
                "SELECT *, i.quantity as iquantity FROM product p JOIN order_item i ON i.product_id=p.product_id WHERE i.order_id=" + id,
                resultSet -> {
                    try {
                        final var items = new ArrayList<OrderProd>();
                        while (resultSet.next()) {
                            items.add(new OrderProdBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .setPrice(resultSet.getInt("price"))
                                    .setQuantity(resultSet.getInt("iquantity"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public List<OrderItem> readOrderItemByOrderIdAndProductId(int order, int item){
        return this.databaseService.performSQL(
                "SELECT * FROM order_item WHERE order_id=" + order + " AND product_id=" + item,
                resultSet -> {
                    try {
                        final var items = new ArrayList<OrderItem>();
                        while (resultSet.next()) {
                            items.add(new OrderItemBuilder()
                                    .setId(resultSet.getInt("order_item_id"))
                                    .setOrder(resultSet.getInt("order_id"))
                                    .setProduct(resultSet.getInt("product_id"))
                                    .setQuantity(resultSet.getInt("quantity"))
                                    .build()
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public void updateOrderItemQuantityByOrderIdAndProductId(int order, int item, int updatedQuantity){
        final var oldOrderItem = readOrderItemByOrderIdAndProductId(order, item);
        this.databaseService.performDML(String.format(
                "UPDATE order_item SET quantity=%d WHERE order_id=%d AND product_id=%d",
                updatedQuantity, order, item
        ));
    }

    //may be deleted in the future
    public void deleteOrderItemById(int id){
        this.databaseService.performDML(String.format("DELETE FROM order_item WHERE order_item_id=%d", id));
    }

    public void deleteOrderItemByOrderIdAndItemId(int order, int item){
        this.databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d AND product_id=%d", order, item));
    }

    public void deleteOrderItemByOrderId(int order){
        this.databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d", order));
    }

    public void deleteOrderItemByOrderIdIfQuantityEqualsZero(int order){
        this.databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d AND quantity=0", order));
    }
}
