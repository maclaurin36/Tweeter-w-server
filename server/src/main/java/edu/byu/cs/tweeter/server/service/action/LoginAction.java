package edu.byu.cs.tweeter.server.service.action;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.action.authenticated.AuthenticatedAction;
import edu.byu.cs.tweeter.server.service.utility.AuthTokenGenerator;
import edu.byu.cs.tweeter.server.service.utility.HashUtility;

public class LoginAction extends AuthenticateAction {
    public LoginAction(UserDao userDao, AuthDao authDao, LoginRequest request) {
        super(userDao, authDao, request);
    }

    public AuthenticateResponse authenticate() {
        FullUser userWithPassword = userDao.getUser(loginRequest.getAlias());

        if (userWithPassword == null) {
            return new AuthenticateResponse("Invalid alias");
        }

        try {
            if (!HashUtility.validatePassword(loginRequest.getPassword(), userWithPassword.getPassword())) {
                return new AuthenticateResponse("Invalid password");
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("[Bad Request] Invalid password specified", ex);
        }

        AuthToken authToken = AuthTokenGenerator.generateAuthToken();

        authDao.insertAuthToken(authToken);
        User user = new User(userWithPassword.getFirstName(), userWithPassword.getLastName(), userWithPassword.getAlias(), userWithPassword.getImageUrl());
        return new AuthenticateResponse(user, authToken);
    }
}
