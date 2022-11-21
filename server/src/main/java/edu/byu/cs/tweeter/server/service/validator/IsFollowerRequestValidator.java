package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class IsFollowerRequestValidator extends AuthenticatedValidator {
    private IsFollowerRequest request;

    public IsFollowerRequestValidator(IsFollowerRequest request, UserDao userDao) {
        super(request, userDao);
        this.request = request;
    }

    @Override
    protected void validateRequest() {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a follower alias");
        } else if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        }
    }
}
