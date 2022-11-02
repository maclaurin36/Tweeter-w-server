package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest {
    private String alias;
    private String firstname;
    private String lastname;
    private String image;
    private String password;

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

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private RegisterRequest() {}
}
