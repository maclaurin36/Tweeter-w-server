package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.FollowingCountObserver;

public class FollowingCountHandler extends BackgroundTaskHandler<FollowingCountObserver> {

    public FollowingCountHandler(FollowingCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        int count = data.getInt(GetFollowingCountTask.COUNT_KEY);
        observer.handleGetFollowingCountSucceeded(count);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to get following count";
    }
}