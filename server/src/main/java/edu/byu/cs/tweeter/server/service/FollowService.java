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
import edu.byu.cs.tweeter.server.service.validator.FollowUnfollowRequestValidator;
import edu.byu.cs.tweeter.server.service.validator.IsFollowerRequestValidator;
import edu.byu.cs.tweeter.server.service.validator.PagedRequestValidator;
import edu.byu.cs.tweeter.server.service.validator.UserRequestValidator;

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
        PagedRequestValidator<String> pagedRequestValidator = new PagedRequestValidator<String>(request, daoFactory.getAuthDao());
        pagedRequestValidator.validate();
        List<String> aliases = daoFactory.getFollowDao().getFollowers(request.getAlias(), request.getLimit(), request.getLastItem());
        for (String alias : aliases) {
            System.out.println(alias);
        }
        List<FullUser> fullUsers = daoFactory.getUserDao().batchGetUser(aliases);
        List<User> users = fullUsers.stream().map(fullUser -> new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl())).collect(Collectors.toList());
        return new PagedResponse<>(true, !(users.size() < request.getLimit()), users);
    }

    public PagedResponse<User> getFollowing(PagedRequest<String> request) {
        PagedRequestValidator<String> pagedRequestValidator = new PagedRequestValidator<String>(request, daoFactory.getAuthDao());
        pagedRequestValidator.validate();
        List<String> aliases = daoFactory.getFollowDao().getFollowing(request);
        List<FullUser> fullUsers = daoFactory.getUserDao().batchGetUser(aliases);
        List<User> users = fullUsers.stream().map(fullUser -> new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl())).collect(Collectors.toList());
        return new PagedResponse<>(true, !(users.size() < request.getLimit()), users);
    }

    public CountResponse getFollowerCount(UserRequest request) {
        UserRequestValidator userRequestValidator = new UserRequestValidator(request, daoFactory.getAuthDao());
        userRequestValidator.validate();
        FullUser user = daoFactory.getUserDao().getUser(request.getAlias());
        int count = user.getFollowerCount();
        return new CountResponse(count);
    }

    public CountResponse getFollowingCount(UserRequest request) {
        UserRequestValidator userRequestValidator = new UserRequestValidator(request, daoFactory.getAuthDao());
        userRequestValidator.validate();
        FullUser user = daoFactory.getUserDao().getUser(request.getAlias());
        int count = user.getFollowingCount();
        return new CountResponse(count);
    }

    public IsFollowerResponse getIsFollower(IsFollowerRequest request) {
        IsFollowerRequestValidator isFollowerRequestValidator = new IsFollowerRequestValidator(request, daoFactory.getAuthDao());
        isFollowerRequestValidator.validate();
        Boolean doesFollow = daoFactory.getFollowDao().checkFollows(request);
        return new IsFollowerResponse(doesFollow);
    }
}
