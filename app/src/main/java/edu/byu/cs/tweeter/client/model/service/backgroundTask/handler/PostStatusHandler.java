package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.PostStatusObserver;

public class PostStatusHandler extends BackgroundTaskHandler<PostStatusObserver> {

    public PostStatusHandler(PostStatusObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        observer.handlePostStatusSuccess();
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to post status";
    }

    @Override
    protected void handleTaskFailure(String message) {
        if (message.contains("exception")) {
            message = message.substring(message.lastIndexOf(" because of exception: "));
            observer.handlePostStatusException(new Exception(message));
        }
        else {
            message = message.substring(message.lastIndexOf(getFailureMessage()));
            observer.handleFailure(message);
        }
    }
}