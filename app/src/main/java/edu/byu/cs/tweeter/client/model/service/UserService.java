package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.authenticated.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.authenticated.RegisterHandler;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;

public class UserService extends BaseService {
    public static final String LOGIN_URL_PATH = "/login";
    public static final String GET_USER_URL_PATH = "/getuser";
    public static final String REGISTER_URL_PATH = "/register";
    public static final String LOGOUT_URL_PATH = "/logout";

    public void login(String username, String password, AuthenticateObserver observer) {
        // Run the login task in the background to log the user in
        // Send the login request.
        LoginTask loginTask = new LoginTask(username, password, new LoginHandler(observer));
        BackgroundTaskUtils.runTask(loginTask);
    }

    public void getUser(String username, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(), username, new GetUserHandler(observer));
        BackgroundTaskUtils.runTask(getUserTask);
    }

    public void registerUser(String firstName, String lastName, String alias, String password, String imageBytesBase64, AuthenticateObserver observer) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new RegisterHandler(observer));
        BackgroundTaskUtils.runTask(registerTask);
    }

    public void logout(LogoutObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        BackgroundTaskUtils.runTask(logoutTask);
    }
}
