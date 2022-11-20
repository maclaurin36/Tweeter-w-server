package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;

public interface UserDao {
    FullUser getUser(String alias);
    Boolean deleteAuthToken(AuthToken authToken);
    void insertAuthToken(String alias, AuthToken authToken);
    void insertUser(FullUser user);
}
