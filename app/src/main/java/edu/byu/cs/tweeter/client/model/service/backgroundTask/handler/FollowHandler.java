package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;

public class FollowHandler extends BackgroundTaskHandler<ToggleFollowObserver> {

    public FollowHandler(ToggleFollowObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        observer.handleToggleFollowSucceeded(false);
    }

    @Override
    protected void handleTaskFailure(String message) {
        observer.handleToggleFollowFailed();
        observer.handleFailure(message);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to follow";
    }
}