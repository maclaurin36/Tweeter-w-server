package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<IsFollowerObserver> {

    public IsFollowerHandler(IsFollowerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleIsFollowerSucceeded(isFollower);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to determine following relationship";
    }
}