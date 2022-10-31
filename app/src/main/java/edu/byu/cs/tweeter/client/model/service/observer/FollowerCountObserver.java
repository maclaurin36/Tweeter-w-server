package edu.byu.cs.tweeter.client.model.service.observer;

public interface FollowerCountObserver extends ServiceObserver {
    void handleGetFollowerCountSucceeded(int count);
}
