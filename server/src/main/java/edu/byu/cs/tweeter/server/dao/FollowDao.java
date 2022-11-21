package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserPagedResponse;

public interface FollowDao {
    Boolean insertFollower(String userToBeFollowedAlias, String userToFollowAlias);
    Boolean deleteFollower(String userThatWasFollowed, String userNoLongerFollowing);

    List<String> getFollowers(String alias, int limit, String lastItem);
    List<String> getFollowing(PagedRequest<String> request);

    Boolean checkFollows(IsFollowerRequest userRequest);
}
