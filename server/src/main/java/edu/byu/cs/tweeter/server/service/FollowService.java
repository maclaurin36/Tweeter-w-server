package edu.byu.cs.tweeter.server.service;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
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

    public Response follow(FollowUnfollowRequest request) {
        RequestValidator.validateFollowUnfollowRequest(request);
        Boolean insertSucceeded = daoFactory.getFollowDao().insertFollower(request.getUserToFollowUnfollowAlias(), request.getUserAlias());
        daoFactory.getUserDao().incrementFollowerCount(request.getUserToFollowUnfollowAlias());
        daoFactory.getUserDao().incrementFollowingCount(request.getUserAlias());
        return new Response(insertSucceeded);
    }

    public Response unfollow(FollowUnfollowRequest request) {
        RequestValidator.validateFollowUnfollowRequest(request);
        Boolean deleteSucceeded = daoFactory.getFollowDao().deleteFollower(request.getUserToFollowUnfollowAlias(), request.getUserAlias());
        daoFactory.getUserDao().decrementFollowerCount(request.getUserToFollowUnfollowAlias());
        daoFactory.getUserDao().decrementFollowingCount(request.getUserAlias());
        return new Response(deleteSucceeded);
    }

    public PagedResponse<User> getFollowers(PagedRequest<String> request) {
        RequestValidator.validatePagedRequest(request);
        List<String> aliases = daoFactory.getFollowDao().getFollowers(request);
        for (String alias : aliases) {
            System.out.println(alias);
        }
        List<FullUser> fullUsers = daoFactory.getUserDao().batchGetUser(aliases);
        List<User> users = fullUsers.stream().map(fullUser -> new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl())).collect(Collectors.toList());
        return new PagedResponse<>(true, !(users.size() < request.getLimit()), users);
    }

    public PagedResponse<User> getFollowing(PagedRequest<String> request) {
        RequestValidator.validatePagedRequest(request);
        List<String> aliases = daoFactory.getFollowDao().getFollowing(request);
        List<FullUser> fullUsers = daoFactory.getUserDao().batchGetUser(aliases);
        List<User> users = fullUsers.stream().map(fullUser -> new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl())).collect(Collectors.toList());
        return new PagedResponse<>(true, !(users.size() < request.getLimit()), users);
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
