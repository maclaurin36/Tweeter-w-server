package edu.byu.cs.tweeter.server.service;

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
import edu.byu.cs.tweeter.server.service.action.paged.FollowerPagedAction;
import edu.byu.cs.tweeter.server.service.action.paged.FollowingPagedAction;
import edu.byu.cs.tweeter.server.service.action.count.FollowerCountAction;
import edu.byu.cs.tweeter.server.service.action.count.FollowingCountAction;
import edu.byu.cs.tweeter.server.service.validator.FollowUnfollowRequestValidator;
import edu.byu.cs.tweeter.server.service.validator.IsFollowerRequestValidator;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {

    public FollowService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public Response follow(FollowUnfollowRequest request) {
        FollowUnfollowRequestValidator followUnfollowRequestValidator = new FollowUnfollowRequestValidator(request, daoFactory.getAuthDao());
        followUnfollowRequestValidator.validate();
        Boolean insertSucceeded = daoFactory.getFollowDao().insertFollower(request.getUserToFollowUnfollowAlias(), request.getUserAlias());
        FullUser userToFollowUnfollow = daoFactory.getUserDao().getUser(request.getUserToFollowUnfollowAlias());
        userToFollowUnfollow.setFollowerCount(userToFollowUnfollow.getFollowerCount() + 1);
        daoFactory.getUserDao().updateUser(userToFollowUnfollow);
        FullUser requestUser = daoFactory.getUserDao().getUser(request.getUserAlias());
        requestUser.setFollowingCount(requestUser.getFollowerCount() + 1);
        daoFactory.getUserDao().updateUser(requestUser);
        return new Response(insertSucceeded);
    }

    public Response unfollow(FollowUnfollowRequest request) {
        FollowUnfollowRequestValidator followUnfollowRequestValidator = new FollowUnfollowRequestValidator(request, daoFactory.getAuthDao());
        followUnfollowRequestValidator.validate();
        Boolean deleteSucceeded = daoFactory.getFollowDao().deleteFollower(request.getUserToFollowUnfollowAlias(), request.getUserAlias());

        FullUser userToFollowUnfollow = daoFactory.getUserDao().getUser(request.getUserToFollowUnfollowAlias());
        userToFollowUnfollow.setFollowerCount(userToFollowUnfollow.getFollowerCount() - 1);
        daoFactory.getUserDao().updateUser(userToFollowUnfollow);
        FullUser requestUser = daoFactory.getUserDao().getUser(request.getUserAlias());
        requestUser.setFollowingCount(requestUser.getFollowerCount() - 1);
        daoFactory.getUserDao().updateUser(requestUser);
        return new Response(deleteSucceeded);
    }

    public PagedResponse<User> getFollowers(PagedRequest<String> request) {
        return new FollowerPagedAction(daoFactory.getAuthDao(), daoFactory.getFollowDao(), daoFactory.getUserDao()).getList(request);
    }

    public PagedResponse<User> getFollowing(PagedRequest<String> request) {
        return new FollowingPagedAction(daoFactory.getAuthDao(), daoFactory.getFollowDao(), daoFactory.getUserDao()).getList(request);
    }

    public CountResponse getFollowerCount(UserRequest request) {
        return new FollowerCountAction().getCount(request, daoFactory.getAuthDao(), daoFactory.getUserDao());
    }

    public CountResponse getFollowingCount(UserRequest request) {
        return new FollowingCountAction().getCount(request, daoFactory.getAuthDao(), daoFactory.getUserDao());
    }

    public IsFollowerResponse getIsFollower(IsFollowerRequest request) {
        IsFollowerRequestValidator isFollowerRequestValidator = new IsFollowerRequestValidator(request, daoFactory.getAuthDao());
        isFollowerRequestValidator.validate();
        Boolean doesFollow = daoFactory.getFollowDao().getFollows(request);
        return new IsFollowerResponse(doesFollow);
    }
}
