package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.FollowDAO;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDAO} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public PagedResponse<User> getFollowees(PagedRequest request) {
        ServiceUtils.validatePagedRequest(request);
        return getFollowingDAO().getFollowees(request);
    }

    public PagedResponse<User> getFollowers(PagedRequest request) {
        ServiceUtils.validatePagedRequest(request);
        return getFollowingDAO().getFollowers(request);
    }

    public Response follow(UserRequest request) {
        return null;
    }

    public CountResponse getFollowerCount(UserRequest request) {
        return null;
    }

    public CountResponse getFollowingCount(UserRequest request) {
        return null;
    }

    public IsFollowerResponse getIsFollower(UserRequest request) {
        return null;
    }

    public Response unfollow(UserRequest request) {
        return null;
    }

    /**
     * Returns an instance of {@link FollowDAO}. Allows mocking of the FollowDAO class
     * for testing purposes. All usages of FollowDAO should get their FollowDAO
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDAO getFollowingDAO() {
        return new FollowDAO();
    }
}
