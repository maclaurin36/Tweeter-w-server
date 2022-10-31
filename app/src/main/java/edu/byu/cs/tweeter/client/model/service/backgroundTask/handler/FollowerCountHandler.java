package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.FollowerCountObserver;

public class FollowerCountHandler extends BackgroundTaskHandler<FollowerCountObserver> {
    public FollowerCountHandler(FollowerCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.handleGetFollowerCountSucceeded(count);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to get follower count";
    }
}