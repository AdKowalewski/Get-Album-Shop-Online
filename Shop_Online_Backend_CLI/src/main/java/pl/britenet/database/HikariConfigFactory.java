package pl.britenet.database;

import com.zaxxer.hikari.HikariConfig;

public class HikariConfigFactory {

    public static HikariConfig prepareDevRootConfig(){
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/shop_online");
        config.setUsername("root");
        config.setPassword("");
        return config;
    }
}