package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthDao {
    Boolean deleteAuthToken(AuthToken authToken);
    AuthToken getAuthToken(AuthToken authToken);
    void insertAuthToken(AuthToken authToken);
}
