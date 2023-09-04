package pl.britenet.authorization;

public class LoginStatus {

    boolean loggedIn;

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public LoginStatus setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
        return this;
    }
}
