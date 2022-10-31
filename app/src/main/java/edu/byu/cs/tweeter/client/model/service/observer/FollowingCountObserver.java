package edu.byu.cs.tweeter.client.model.service.observer;

public interface FollowingCountObserver extends ServiceObserver {
    void handleGetFollowingCountSucceeded(int count);
}
