package edu.byu.cs.tweeter.client.model.service.observer;

public interface ToggleFollowObserver extends ServiceObserver {
    void handleToggleFollowSucceeded(Boolean value);
    void handleToggleFollowFailed();
}
