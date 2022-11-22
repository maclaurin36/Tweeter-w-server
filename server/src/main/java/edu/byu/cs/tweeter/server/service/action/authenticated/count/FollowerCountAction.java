package edu.byu.cs.tweeter.server.service.action.authenticated.count;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;

public class FollowerCountAction extends CountAction {
    public FollowerCountAction(UserDao userDao, AuthDao authDao, UserRequest userRequest) {
        super(userDao, authDao, userRequest);

    }
    @Override
    protected int getSpecificCount(FullUser user) {
        return user.getFollowerCount();
    }
}
