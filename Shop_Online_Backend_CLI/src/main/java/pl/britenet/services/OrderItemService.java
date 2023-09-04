package pl.britenet.services;

import pl.britenet.database.DatabaseService;
import pl.britenet.models.OrderItem;
import pl.britenet.models.Product;
import pl.britenet.models.builders.OrderItemBuilder;
import pl.britenet.models.builders.ProductBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderItemService {

    private static final DatabaseService databaseService = DatabaseService.getInstance();


    public static void createOrderItem(Integer order, Integer product, int quantity){
        databaseService.performDML(String.format(
                        "INSERT INTO order_item (order_id, product_id, quantity) VALUES (%d, %d, %d)",
                        order, product, quantity
                )
        );
    }

    //may be deleted in the future
    public static List<OrderItem> readAllOrderItems(){
        return databaseService.performSQL(
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

    public static Map<Integer, Integer> readOrderItemsByOrderId(int id){
        return databaseService.performSQL(
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
    public static Integer getProductQuantityByOrderId(int order, int product){
        return databaseService.performSQL(
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

    public static List<Product> readProductsByOrderId(int id){
        return databaseService.performSQL(
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

    public static Map<Product, Integer> readProductsWithQuantitiesByOrderId(int id){
        return databaseService.performSQL(
                "SELECT *, i.quantity FROM product p JOIN order_item i ON i.product_id=p.product_id WHERE i.order_id=" + id,
                resultSet -> {
                    try {
                        final var items = new HashMap<Product, Integer>();
                        while (resultSet.next()) {
                            items.put(new ProductBuilder()
                                    .setId(resultSet.getInt("product_id"))
                                    .setArtist(resultSet.getString("artist"))
                                    .setTitle(resultSet.getString("title"))
                                    .setGenre(resultSet.getString("genre"))
                                    .setLabel(resultSet.getString("label"))
                                    .setReleaseYear(resultSet.getInt("release_year"))
                                    .build(),
                                    resultSet.getInt("quantity")
                            );
                        }
                        return items;
                    } catch (SQLException e) {
                        throw new IllegalStateException(e);
                    }
                }).get();
    }

    public static List<OrderItem> readOrderItemByOrderIdAndProductId(int order, int item){
        return databaseService.performSQL(
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

    public static void updateOrderItemQuantityByOrderIdAndProductId(int order, int item, int updatedQuantity){
        final var oldOrderItem = readOrderItemByOrderIdAndProductId(order, item);
        databaseService.performDML(String.format(
                "UPDATE order_item SET quantity=%d WHERE order_id=%d AND product_id=%d",
                updatedQuantity, order, item
        ));
    }

    //may be deleted in the future
    public static void deleteOrderItemById(int id){
        databaseService.performDML(String.format("DELETE FROM order_item WHERE order_item_id=%d", id));
    }

    public static void deleteOrderItemByOrderIdAndItemId(int order, int item){
        databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d AND product_id=%d", order, item));
    }

    public static void deleteOrderItemByOrderId(int order){
        databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d", order));
    }

    public static void deleteOrderItemByOrderIdIfQuantityEqualsZero(int order){
        databaseService.performDML(String.format("DELETE FROM order_item WHERE order_id=%d AND quantity=0", order));
    }
}
