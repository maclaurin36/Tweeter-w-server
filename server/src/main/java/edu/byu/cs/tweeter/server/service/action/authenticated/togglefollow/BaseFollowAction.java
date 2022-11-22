package edu.byu.cs.tweeter.server.service.action.authenticated.togglefollow;

import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.action.authenticated.AuthenticatedAction;

public abstract class BaseFollowAction extends AuthenticatedAction {
    protected FollowDao followDao;
    private UserDao userDao;
    private FollowUnfollowRequest request;

    public BaseFollowAction(FollowDao followDao, UserDao userDao, AuthDao authDao, FollowUnfollowRequest request) {
        super(authDao, request);
        this.followDao = followDao;
        this.userDao = userDao;
        this.request = request;

        validate();
    }

    public Response run() {
        performFollowUnfollow(request.getUserToFollowUnfollowAlias(), request.getUserAlias());

        FullUser userToFollowUnfollow = userDao.getUser(request.getUserToFollowUnfollowAlias());
        userToFollowUnfollow.setFollowerCount(getNewFollowerCount(userToFollowUnfollow.getFollowerCount()));
        userDao.updateUser(userToFollowUnfollow);
        FullUser requestUser = userDao.getUser(request.getUserAlias());
        requestUser.setFollowingCount(getNewFollowingCount(requestUser.getFollowingCount()));
        userDao.updateUser(requestUser);
        return new Response(true);
    }

    private void validate() {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (request.getUserToFollowUnfollowAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to follow/unfollow alias");
        }
    }

    protected abstract void performFollowUnfollow(String userToBeFollowedUnfollowed, String user);
    protected abstract int getNewFollowerCount(int count);
    protected abstract int getNewFollowingCount(int count);
}
