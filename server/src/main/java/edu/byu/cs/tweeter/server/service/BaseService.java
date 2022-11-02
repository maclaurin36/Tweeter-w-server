package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

public abstract class BaseService {
    FollowDAO getFollowDao() {
        return new FollowDAO();
    }
    UserDAO getUserDao() { return new UserDAO(); }
}
