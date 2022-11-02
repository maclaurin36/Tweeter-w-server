package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class UserResponse extends Response {

    private User user;

    public UserResponse(User user) {
        super(true);
        this.user = user;
    }

    UserResponse(boolean success, String errorMessage) {
        super(success, errorMessage);
    }

    public User getUser() {
        return user;
    }
}
