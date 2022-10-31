package edu.byu.cs.tweeter.client.presenter.authenticate;

import edu.byu.cs.tweeter.client.model.service.observer.AuthenticateObserver;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticatePresenter extends BasePresenter<AuthenticateView> implements AuthenticateObserver {

    public AuthenticatePresenter(AuthenticateView view) {
        super(view);
    }

    public String validateLoginCredentials(String alias, String password) {
        if(alias.length() == 0) {
            return "Alias cannot be empty.";
        }
        if (alias.length() < 2) {
            return "Alias must contain 1 or more characters after the @.";
        }
        if (alias.charAt(0) != '@') {
            return "Alias must begin with @.";
        }
        if (password.length() == 0) {
            return "Password cannot be empty.";
        }
        return "";
    }

    protected boolean handleErrorMessage(String errorMessage) {
        view.clearValidationMessage();
        if (!errorMessage.equals("")) {
            view.displayErrorMessage(errorMessage);
            return true;
        }
        return false;
    }

    @Override
    public void handleAuthenticationSucceeded(User user, AuthToken authToken) {
        view.displayInfoMessage("Hello " + user.getName());
        try {
            view.navigateToUser(user);
        } catch (Exception e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
}
