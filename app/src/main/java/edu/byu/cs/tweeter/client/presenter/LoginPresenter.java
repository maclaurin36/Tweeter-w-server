package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;

/**
 * The presenter for the login functionality of the application.
 */
public class LoginPresenter implements AuthenticateObserver {

    private static final String LOG_TAG = "LoginPresenter";

    private final View view;

    @Override
    public void handleAuthenticationSucceeded(User user, AuthToken authToken) {
        view.loginSuccessful(user, authToken);
    }

    /**
     * The interface by which this presenter communicates with it's view.
     */
    public interface View {
        void loginSuccessful(User user, AuthToken authToken);
        void loginUnsuccessful(String message);
    }

    /**
     * Creates an instance.
     *
     * @param view the view for which this class is the presenter.
     */
    public LoginPresenter(View view) {
        // An assertion would be better, but Android doesn't support Java assertions
        if(view == null) {
            throw new NullPointerException();
        }
        this.view = view;
    }

    /**
     * Initiates the login process.
     *
     * @param username the user's username.
     * @param password the user's password.
     */
    public void initiateLogin(String username, String password) {
        UserService userService = new UserService();
        userService.login(username, password, this);
    }

    /**
     * Invoked when the login request completes if the login request was unsuccessful. Notifies the
     * view of the unsuccessful login.
     *
     * @param message error message.
     */
    @Override
    public void handleFailure(String message) {
        String errorMessage = "Failed to login: " + message;
        Log.e(LOG_TAG, errorMessage);
        view.loginUnsuccessful(errorMessage);
    }
}
