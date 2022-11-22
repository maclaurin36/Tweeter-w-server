package edu.byu.cs.tweeter.model.net.request;

/**
 * Contains all the information needed to make a login request.
 */
public class LoginRequest {

    protected String alias;
    protected String password;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private LoginRequest() {}

    /**
     * Creates an instance.
     *
     * @param alias the username of the user to be logged in.
     * @param password the password of the user to be logged in.
     */
    public LoginRequest(String alias, String password) {
        this.alias = alias;
        this.password = password;
    }

    /**
     * Returns the username of the user to be logged in by this request.
     *
     * @return the username.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the username.
     *
     * @param alias the username.
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }
    /**
     * Returns the password of the user to be logged in by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
