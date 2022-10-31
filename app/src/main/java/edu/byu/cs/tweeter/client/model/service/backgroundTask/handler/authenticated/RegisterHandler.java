package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.authenticated;

import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;

public class RegisterHandler extends AuthenticateHandler {
    public RegisterHandler(AuthenticateObserver observer) {
        super(observer);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to register";
    }
}
