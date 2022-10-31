package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;

public class UnfollowHandler extends BackgroundTaskHandler<ToggleFollowObserver> {

    public UnfollowHandler(ToggleFollowObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        observer.handleToggleFollowSucceeded(true);
    }

    @Override
    protected void handleTaskFailure(String message) {
        observer.handleToggleFollowFailed();
        observer.handleFailure(message);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to unfollow";
    }
}