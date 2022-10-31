package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.FollowerCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.FollowingCountHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.service.observer.FollowerCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.FollowingCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends BaseService {
    //TODO fix this path
    public static final String URL_PATH = "/getfollowing";
    public void getFollowing(User user, User lastFollowee, PaginationObserver<User> observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollowee, new GetFollowingHandler(observer));
        BackgroundTaskUtils.runTask(getFollowingTask);
    }

    public void getFollowers(User user, User lastFollower, PaginationObserver<User> observer) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollower, new GetFollowersHandler(observer));
        BackgroundTaskUtils.runTask(getFollowersTask);
    }

    public void getFollowingCount(User selectedUser, FollowingCountObserver observer) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowingCountHandler(observer));
        BackgroundTaskUtils.runTask(followingCountTask);
    }

    public void getFollowerCount(User selectedUser, FollowerCountObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowerCountHandler(observer));
        BackgroundTaskUtils.runTask(followersCountTask);
    }

    public void getIsFollower(User selectedUser, IsFollowerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }

    public void unfollow(User user, ToggleFollowObserver observer) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new UnfollowHandler(observer));
        BackgroundTaskUtils.runTask(unfollowTask);
    }

    public void follow(User user, ToggleFollowObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                user, new FollowHandler(observer));
        BackgroundTaskUtils.runTask(followTask);
    }
}