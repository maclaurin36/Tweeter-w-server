package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class UserDAO {
    public User getUser(String alias) {
        return FakeData.getInstance().findUserByAlias(alias);
    }

    public User getUser(AuthToken authToken) {
        return new User("dummyFirstName","dummyLastName","someAlias","dummyImageUrl");
    }
}
