package edu.byu.cs.tweeter.server.service.action.authenticated;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.FollowDao;

public class IsFollowerAction extends AuthenticatedAction {
    private FollowDao followDao;
    private IsFollowerRequest request;

    public IsFollowerAction(FollowDao followDao, AuthDao authDao, IsFollowerRequest request) {
        super(authDao, request);
        this.followDao = followDao;
        this.request = request;

        validate();
    }

    public IsFollowerResponse getIsFollower() {
        Boolean doesFollow = followDao.getFollows(request);
        return new IsFollowerResponse(doesFollow);
    }

    private void validate() {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
    }
}
