package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest extends LoginRequest {
    private String firstname;
    private String lastname;
    private String image;

    public RegisterRequest(String alias, String firstname, String lastname, String image, String password) {
        super(alias, password);
        this.firstname = firstname;
        this.lastname = lastname;
        this.image = image;
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

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setImage(String image) {
        this.image = image;
    }

    private RegisterRequest() { super(null, null); }
}
