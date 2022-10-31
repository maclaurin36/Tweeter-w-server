package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.authenticated.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;

/**
 * Contains the business logic to support the login operation.
 */
public class UserService {

    public static final String URL_PATH = "/login";

    /**
     * Creates an instance.
     *
     */
     public UserService() {
     }

    /**
     * Makes an asynchronous login request.
     *
     * @param username the user's name.
     * @param password the user's password.
     */
    public void login(String username, String password, AuthenticateObserver observer) {
        LoginTask loginTask = getLoginTask(username, password, observer);
        BackgroundTaskUtils.runTask(loginTask);
    }

    /**
     * Returns an instance of {@link LoginTask}. Allows mocking of the LoginTask class for
     * testing purposes. All usages of LoginTask should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    LoginTask getLoginTask(String username, String password, AuthenticateObserver observer) {
        return new LoginTask(this, username, password, new LoginHandler(observer));
    }
}
