package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;

public interface UserDao {
    FullUser getUser(String alias);
    void insertUser(FullUser user);
    List<FullUser> batchGetUser(List<String> aliases);
    void updateUser(FullUser user);
}
