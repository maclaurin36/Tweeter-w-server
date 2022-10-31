package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.authenticated;

import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;

public class LoginHandler extends AuthenticateHandler {

    public LoginHandler(AuthenticateObserver observer) {
        super(observer);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to login";
    }
}
