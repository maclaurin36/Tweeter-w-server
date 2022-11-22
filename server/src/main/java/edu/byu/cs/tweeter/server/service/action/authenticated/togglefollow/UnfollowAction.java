package edu.byu.cs.tweeter.server.service.action.authenticated.togglefollow;

import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class UnfollowAction extends BaseFollowAction{

    public UnfollowAction(FollowDao followDao, UserDao userDao, AuthDao authDao, FollowUnfollowRequest request) {
        super(followDao, userDao, authDao, request);
    }

    @Override
    protected void performFollowUnfollow(String userToBeUnfollowedFollowed, String originalUser) {
        followDao.deleteFollower(userToBeUnfollowedFollowed, originalUser);
    }

    @Override
    protected int getNewFollowerCount(int count) {
        return count - 1;
    }

    @Override
    protected int getNewFollowingCount(int count) {
        return count - 1;
    }
}
