package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.utility.RequestValidator;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {

    public FollowService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public Response follow(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        Boolean insertSucceeded = daoFactory.getFollowDao().insertFollower(request);
        return new Response(insertSucceeded);

        // Insert into the follows table (both as a follower and as a followee)
        // Increment the follower count of the user with that request alias
        // Increment the following count of the user with
    }

    public Response unfollow(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        Boolean deleteSucceeded = daoFactory.getFollowDao().deleteFollower(request);
        return new Response(deleteSucceeded);
    }

    public PagedResponse<User> getFollowers(PagedRequest<String> request) {
        RequestValidator.validatePagedRequest(request);
        return daoFactory.getFollowDao().getFollowers(request);
    }

    public PagedResponse<User> getFollowing(PagedRequest<String> request) {
        RequestValidator.validatePagedRequest(request);
        return daoFactory.getFollowDao().getFollowing(request);
    }

    public CountResponse getFollowerCount(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        FullUser user = daoFactory.getUserDao().getUser(request.getAlias());
        int count = user.getFollowerCount();
        return new CountResponse(count);
    }

    public CountResponse getFollowingCount(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        FullUser user = daoFactory.getUserDao().getUser(request.getAlias());
        int count = user.getFollowingCount();
        return new CountResponse(count);
    }

    public IsFollowerResponse getIsFollower(IsFollowerRequest request) {
        RequestValidator.validateIsFollowerRequest(request);
        Boolean doesFollow = daoFactory.getFollowDao().checkFollows(request);
        return new IsFollowerResponse(doesFollow);
    }
}
