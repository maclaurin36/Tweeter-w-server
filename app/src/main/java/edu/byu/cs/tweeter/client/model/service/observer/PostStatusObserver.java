package edu.byu.cs.tweeter.client.model.service.observer;

public interface PostStatusObserver extends ServiceObserver {
    void handlePostStatusSuccess();
    void handlePostStatusException(Exception ex);
}