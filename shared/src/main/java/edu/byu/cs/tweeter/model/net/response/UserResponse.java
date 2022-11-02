package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class UserResponse extends Response {

    private User user;

    UserResponse(boolean success, User user) {
        super(success);
        this.user = user;
    }

    UserResponse(boolean success, String errorMessage) {
        super(success, errorMessage);
    }

    public User getUser() {
        return user;
    }
}
