package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserDAO {
    public User getUser(String alias) {
        return new User("dummyFirstName","dummyLastName",alias,"dummyImageUrl");
    }

    public User getUser(AuthToken authToken) {
        return new User("dummyFirstName","dummyLastName","someAlias","dummyImageUrl");
    }
}
