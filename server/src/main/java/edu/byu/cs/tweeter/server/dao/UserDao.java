package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;

public interface UserDao {
    AuthToken getAuthToken(LoginRequest request);
    FullUser getUser(String alias);
    Boolean invalidateAuthToken(AuthToken authToken);
    Boolean insertAuthToken(String alias, AuthToken authToken);
    void insertUser(FullUser user);

    AuthenticateResponse register(RegisterRequest request);
}
