package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public class UserPagedResponse extends PagedResponse<User> {
    public UserPagedResponse(String message) {
        super(message);
    }

    public UserPagedResponse(boolean success, boolean hasMorePages, List<User> items) {
        super(success, hasMorePages, items);
    }
}
