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
import pl.britenet.models.Order;
import pl.britenet.models.OrderProd;
import pl.britenet.models.Product;
import pl.britenet.services.ClientService;
import pl.britenet.services.OrderItemService;
import pl.britenet.services.OrderService;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final ClientService clientService;

    private final UserService userService;

    private final OrderItemService orderItemService;

    private final AuthorizationController authorizationController;

    public OrderController(OrderService orderService, ClientService clientService, UserService userService, OrderItemService orderItemService, AuthorizationController authorizationController) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.authorizationController = authorizationController;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Order create(@RequestBody Order order, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            this.orderService.createOrder(
                    order.getClient().getId(),
                    order.getAddress().getId(),
                    order.getProducts()
            );
            return this.orderService.getLastInsert();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping
    public List<Order> readAll(@RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            return this.orderService.readAllOrders();
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/client/{id}")
    public List<Order> readByCurrentClient(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.clientService.getClientObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Client " + id + " not found."));
            return this.orderService.readCurrentClientOrders(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}/products")
    public List<OrderProd> readProductsFromOrder(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)){
            return this.orderItemService.readProductsWithQuantitiesByOrderId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{id}")
    public Optional<Order> getById(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            return Optional.ofNullable(Optional.ofNullable(this.orderService.getOrderObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Order " + id + " not found.")));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/newest")
    public Optional<Order> getNewest(@RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            return Optional.ofNullable(Optional.ofNullable(this.orderService.getNewestOrder()).orElseThrow(() -> new ResourceNotFoundException("Order not found.")));
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public Order updateById(@PathVariable int id, @RequestBody Order order, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            try {
                this.orderService.updateOrderById(
                        id,
                        order.getProducts(),
                        order.getAddress()
                );
            } catch (Exception e) {
                throw new ResourceNotFoundException("Order " + id + " not found.");
            }
            return this.orderService.getOrderObjectById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.orderService.getOrderObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Order " + id + " not found."));
            this.orderService.deleteOrderById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/client/{id}")
    public void deleteByClientId(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.clientService.getClientObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("Client " + id + " not found."));
            this.orderService.deleteOrdersByClientId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}