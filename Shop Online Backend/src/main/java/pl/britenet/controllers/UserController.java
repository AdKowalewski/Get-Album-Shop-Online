package pl.britenet.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.britenet.authorization.AuthorizationController;
import pl.britenet.exceptions.ResourceNotFoundException;
import pl.britenet.models.User;
import pl.britenet.services.UserService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AuthorizationController authorizationController;

    public UserController(UserService userService, AuthorizationController authorizationController){
        this.userService = userService;
        this.authorizationController = authorizationController;
    }

    @GetMapping
    public List<User> readAll(){
        return this.userService.readAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<User> getById(@PathVariable int id){
        return Optional.ofNullable(Optional.ofNullable(this.userService.getUserObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found.")));
    }

    @GetMapping("/email/{email}")
    public Optional<User> readByEmail(@PathVariable String email){
        return Optional.ofNullable(Optional.ofNullable(this.userService.readUserByEmail(email)).orElseThrow(() -> new ResourceNotFoundException("User " + email + " not found.")));
    }

    @GetMapping("/name/{name}")
    public List<User> readByName(@PathVariable String name){
        return this.userService.readUsersByName(name);
    }

    @GetMapping("/surname/{surname}")
    public List<User> readBySurname(@PathVariable String surname){
        return this.userService.readUsersBySurname(surname);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{id}")
    public User updateById(@RequestBody User user, @PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            try {
                this.userService.updateUserOfGivenId(
                        id,
                        user.getClient().getName(),
                        user.getClient().getSurname(),
                        user.getEmail(),
                        user.getPassword()
                );
            } catch (Exception e) {
                throw new ResourceNotFoundException("User " + id + " not found.");
            }
            return this.userService.getUserObjectById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable int id, @RequestHeader(value = "Authorization") String token){
        if(!token.equals("") && this.userService.isLoggedIn() && this.authorizationController.isTokenLoggedIn(token)) {
            Optional.ofNullable(this.userService.getUserObjectById(id)).orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found."));
            this.userService.deleteUserById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
}
