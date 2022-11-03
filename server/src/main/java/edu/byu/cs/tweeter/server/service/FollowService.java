package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.FollowDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public PagedResponse<User> getFollowees(PagedRequest<String> request) {
        RequestValidator.validatePagedRequest(request);
        return getFollowDao().getFollowees(request);
    }

    public PagedResponse<User> getFollowers(PagedRequest<String> request) {
        RequestValidator.validatePagedRequest(request);
        return getFollowDao().getFollowers(request);
    }

    public Response follow(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        return new Response(true);
    }

    public CountResponse getFollowerCount(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        UserDAO userDAO = getUserDao();
        User user = userDAO.getUser(request.getAlias());
        return new CountResponse(getFollowDao().getFollowerCount(user));
    }

    public CountResponse getFollowingCount(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        UserDAO userDao = getUserDao();
        User user = userDao.getUser(request.getAlias());
        return new CountResponse(getFollowDao().getFolloweeCount(user));
    }

    public IsFollowerResponse getIsFollower(IsFollowerRequest request) {
        RequestValidator.validateIsFollowerRequest(request);
        UserDAO userDao = getUserDao();
        User followeeUser = userDao.getUser(request.getFolloweeAlias());
        User followerUser = userDao.getUser(request.getFollowerAlias());
        return new IsFollowerResponse(getFollowDao().getIsFollower(followeeUser, followerUser));
    }

    public Response unfollow(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        return new Response(true);
    }
}
