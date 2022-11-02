package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The logged-in user returned by the server.
     */
    protected User user;

    /**
     * The auth token returned by the server.
     */
    protected AuthToken authToken;

    private ServerFacade serverFacade;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

//    @Override
//    protected void runTask() {
//        try {
//            LoginRequest request = new LoginRequest(username, password);
//            AuthenticateResponse response = getServerFacade().login(request, UserService.URL_PATH);
//
//            if (response.isSuccess()) {
//                this.user = response.getUser();
//                this.authToken = response.getAuthToken();
//                sendSuccessMessage();
//            } else {
//                sendFailedMessage(response.getMessage());
//            }
//        } catch (Exception ex) {
//            Log.e(LOG_TAG, ex.getMessage(), ex);
//            sendExceptionMessage(ex);
//        }
//    }

    @Override
    protected AuthenticateResponse runAuthenticationTask() throws IOException, TweeterRemoteException {
        LoginRequest request = new LoginRequest(username, password);
        return getServerFacade().login(request, UserService.LOGIN_URL_PATH);
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return serverFacade;
    }
}
