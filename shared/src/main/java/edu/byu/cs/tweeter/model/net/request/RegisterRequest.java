package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest {
    private final String alias;
    private final String firstname;
    private final String lastname;
    private final String image;
    private final String password;

    public RegisterRequest(String alias, String firstname, String lastname, String image, String password) {
        this.alias = alias;
        this.firstname = firstname;
        this.lastname = lastname;
        this.image = image;
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getImage() {
        return image;
    }

    public String getPassword() {
        return password;
    }
}
