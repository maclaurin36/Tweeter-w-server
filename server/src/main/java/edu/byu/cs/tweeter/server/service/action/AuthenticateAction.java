package edu.byu.cs.tweeter.server.service.action;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.utility.AuthTokenGenerator;
import edu.byu.cs.tweeter.server.service.utility.HashUtility;
import edu.byu.cs.tweeter.server.service.validator.RegisterRequestValidator;

public abstract class AuthenticateAction {
    protected UserDao userDao;
    protected AuthDao authDao;
    protected LoginRequest loginRequest;

    public AuthenticateAction(UserDao userDao, AuthDao authDao, LoginRequest request) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.loginRequest = request;

        validate();
    }

    private void validate() {
        if (loginRequest.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a username");
        } else if (loginRequest.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a password");
        }
    }
}
