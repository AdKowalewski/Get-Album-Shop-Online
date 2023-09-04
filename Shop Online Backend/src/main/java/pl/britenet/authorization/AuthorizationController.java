package pl.britenet.authorization;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.britenet.exceptions.EmailTakenException;
import pl.britenet.exceptions.InvalidCredentialsException;
import pl.britenet.models.User;
import pl.britenet.services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class AuthorizationController {

    public static Map<UUID, Integer> tokens = new HashMap<>();

    public Map<UUID, Integer> getTokens() {
        return tokens;
    }

    private final UserService userService;

    public AuthorizationController(UserService userService){
        this.userService = userService;
    }

    UUID generateToken(){
        return UUID.randomUUID();
    }

    @PostMapping("/register")
    public TokenBearerEntity registerUser(@RequestBody User user){
        try {
            this.userService.registerUser(user.getClient().getName(), user.getClient().getSurname(), user.getEmail(), user.getPassword());
            int user_id = this.userService.getLastInsert().getId();
            final var token = this.generateToken();
            tokens.put(token, user_id);
            return new TokenBearerEntity().setToken(token).setUserId(user_id);
        } catch (Exception e){
            throw new EmailTakenException();
        }
    }

    @PostMapping("/login")
    public TokenBearerEntity loginUser(@RequestBody User user){
        User userFromDB = Optional.ofNullable(this.userService.readUserByEmail(user.getEmail())).orElseThrow(() -> {
            throw new InvalidCredentialsException();
        });
        final int user_id = userFromDB.getId();
        if(BCrypt.checkpw(user.getPassword(), userFromDB.getPassword())){
            if(tokens.containsValue(user_id)) {
                for (var entry : tokens.entrySet()) {
                    if(entry.getValue().equals(user_id)) {
                        this.userService.loginUser(user);
                        return new TokenBearerEntity().setToken(entry.getKey()).setUserId(user_id);
                    }
                }
            }else{
                final var token = this.generateToken();
                tokens.put(token, user_id);
                this.userService.loginUser(user);
                return new TokenBearerEntity().setToken(token).setUserId(user_id);
            }

        }throw new InvalidCredentialsException();
    }

    @PostMapping("/is_logged_in")
    public LoginStatus isLoggedIn(@RequestBody TokenBearerEntity token){
        if(tokens.containsKey(token.getToken())){
            return new LoginStatus().setLoggedIn(true);
        } else {
            return new LoginStatus().setLoggedIn(false);
        }
    }

    public boolean isTokenLoggedIn(String token){
        return tokens.containsKey(UUID.fromString(token));
    }

    @PostMapping("/logout/{id}")
    public LogoutStatus userLogout(@PathVariable int id){
        for(var entry : tokens.entrySet()){
            if(entry.getValue().equals(id)){
                this.userService.logOutUser();
                tokens.remove(entry.getKey());
            }
        }
        if(!tokens.containsValue(id)){
            return new LogoutStatus().setMessage("Logged out successfully!");
        } else {
            return new LogoutStatus().setMessage("Failed to log out!");
        }
    }
}