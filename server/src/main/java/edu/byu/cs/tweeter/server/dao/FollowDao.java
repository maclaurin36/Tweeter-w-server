package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserPagedResponse;

public interface FollowDao {
    Boolean insertFollower(UserRequest userRequest);
    Boolean deleteFollower(UserRequest userRequest);

    UserPagedResponse getFollowers(PagedRequest<String> request);
    UserPagedResponse getFollowing(PagedRequest<String> request);

    Boolean checkFollows(IsFollowerRequest userRequest);
}
