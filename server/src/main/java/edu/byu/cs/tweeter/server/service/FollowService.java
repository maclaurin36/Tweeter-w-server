package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

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
import edu.byu.cs.tweeter.server.service.action.BatchFollowersAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.IsFollowerAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.count.FollowerCountAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.count.FollowingCountAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.paged.FollowerPagedAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.paged.FollowingPagedAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.togglefollow.FollowAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.togglefollow.UnfollowAction;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService extends BaseService {

    public FollowService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public Response follow(FollowUnfollowRequest request) {
        return new FollowAction(daoFactory.getFollowDao(), daoFactory.getUserDao(), daoFactory.getAuthDao(), request).run();
    }

    public Response unfollow(FollowUnfollowRequest request) {
        return new UnfollowAction(daoFactory.getFollowDao(), daoFactory.getUserDao(), daoFactory.getAuthDao(), request).run();
    }

    public PagedResponse<User> getFollowers(PagedRequest<String> request) {
        return new FollowerPagedAction(daoFactory.getAuthDao(), daoFactory.getFollowDao(), daoFactory.getUserDao(), request).getList(request);
    }

    public PagedResponse<User> getFollowing(PagedRequest<String> request) {
        return new FollowingPagedAction(daoFactory.getAuthDao(), daoFactory.getFollowDao(), daoFactory.getUserDao(), request).getList(request);
    }

    public CountResponse getFollowerCount(UserRequest request) {
        return new FollowerCountAction(daoFactory.getUserDao(), daoFactory.getAuthDao(), request).getCount();
    }

    public CountResponse getFollowingCount(UserRequest request) {
        return new FollowingCountAction(daoFactory.getUserDao(), daoFactory.getAuthDao(), request).getCount();
    }

    public IsFollowerResponse getIsFollower(IsFollowerRequest request) {
        return new IsFollowerAction(daoFactory.getFollowDao(), daoFactory.getAuthDao(), request).getIsFollower();
    }

    public void batchFollowers(SQSEvent statusEvent) {
        new BatchFollowersAction(daoFactory.getFollowDao(), daoFactory.getQueueDao()).processBatch(statusEvent);
    }
}
