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
        int count = daoFactory.getFollowDao().getFollowingCount(request);
        return new CountResponse(count);
    }

    public CountResponse getFollowingCount(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        int count = daoFactory.getFollowDao().getFollowerCount(request);
        return new CountResponse(count);
    }

    public IsFollowerResponse getIsFollower(IsFollowerRequest request) {
        RequestValidator.validateIsFollowerRequest(request);
        Boolean doesFollow = daoFactory.getFollowDao().checkFollows(request);
        return new IsFollowerResponse(doesFollow);
    }
}
