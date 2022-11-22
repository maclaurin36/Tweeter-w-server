package edu.byu.cs.tweeter.server.service.action;

import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;

public abstract class AuthenticateAction {
    private UserDao userDao;
    private AuthDao authDao;

    public AuthenticateAction(UserDao userDao, AuthDao authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

//    public AuthenticateResponse authenticate() {
//
//    }
}
