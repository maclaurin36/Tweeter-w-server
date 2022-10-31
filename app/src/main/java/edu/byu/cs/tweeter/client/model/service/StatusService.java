package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged.GetFeedHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged.GetStoryHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PostStatusObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends BaseService {

    public void getFeed(User user, Status lastStatus, PaginationObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedHandler(observer));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void getStory(User user, Status lastStatus, PaginationObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetStoryHandler(observer));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void postStatus(Status newStatus, PostStatusObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusHandler(observer));
        BackgroundTaskUtils.runTask(statusTask);
    }
}
