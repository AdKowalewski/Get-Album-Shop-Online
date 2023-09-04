package pl.britenet.authorization;

import java.util.UUID;

public class TokenBearerEntity {

    UUID token;
    int userId;

    public UUID getToken() {
        return token;
    }

    public TokenBearerEntity setToken(UUID token) {
        this.token = token;
        return this;
    }

    public int getId() {
        return userId;
    }

    public TokenBearerEntity setUserId(int user_id) {
        this.userId = user_id;
        return this;
    }
}
