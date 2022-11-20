package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;

public interface UserDao {
    FullUser getUser(String alias);
    Boolean deleteAuthToken(AuthToken authToken);
    void insertAuthToken(String alias, AuthToken authToken);
    void insertUser(FullUser user);
    void incrementFollowerCount(String userAlias);
    void incrementFollowingCount(String userAlias);
    void decrementFollowerCount(String userAlias);
    void decrementFollowingCount(String userAlias);
    List<FullUser> batchGetUser(List<String> aliases);
}
