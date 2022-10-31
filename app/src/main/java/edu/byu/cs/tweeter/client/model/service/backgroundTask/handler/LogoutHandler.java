package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;

public class LogoutHandler extends BackgroundTaskHandler<LogoutObserver> {
    public LogoutHandler(LogoutObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        observer.handleLogoutSuccess();
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to logout";
    }
}