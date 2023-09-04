package pl.britenet.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.britenet.database.DatabaseService;
import pl.britenet.services.AddressService;
import pl.britenet.services.ClientService;
import pl.britenet.services.OrderItemService;
import pl.britenet.services.OrderService;
import pl.britenet.services.ProductService;
import pl.britenet.services.UserService;

@Configuration
public class ServiceConfig {
    private final DatabaseService databaseService;

    @Autowired
    public ServiceConfig(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Bean
    public ProductService getProductService() {
        return new ProductService(this.databaseService);
    }

    @Bean
    public UserService getUserService() {
        return new UserService(this.databaseService);
    }

    @Bean
    public ClientService getClientService() {
        return new ClientService(this.databaseService);
    }

    @Bean
    public AddressService getAddressService() {
        return new AddressService(this.databaseService);
    }

    @Bean
    public OrderItemService getOrderItemService() {
        return new OrderItemService(this.databaseService);
    }

    @Bean
    public OrderService getOrderService() {
        return new OrderService(this.databaseService);
    }
}
