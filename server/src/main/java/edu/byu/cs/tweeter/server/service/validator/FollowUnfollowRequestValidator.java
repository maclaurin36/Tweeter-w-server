package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class FollowUnfollowRequestValidator extends AuthenticatedValidator {
    private FollowUnfollowRequest request;

    public FollowUnfollowRequestValidator(FollowUnfollowRequest request, AuthDao authDao) {
        super(request, authDao);
        this.request = request;
    }

    @Override
    protected void validateRequest() {
        if (request.getUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (request.getUserToFollowUnfollowAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a user to follow/unfollow alias");
        }
    }
}
