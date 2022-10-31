package edu.byu.cs.tweeter.client.presenter.authenticate;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;

public class LoginPresenter extends AuthenticatePresenter {

    public LoginPresenter(AuthenticateView view) {
        super(view);
    }

    public void initiateLogin(String username, String password) {
        String errorMessage = validateLoginCredentials(username, password);
        boolean errorOccurred = handleErrorMessage(errorMessage);
        if (errorOccurred) {
            return;
        }

        view.displayInfoMessage("logging in ...");
        new UserService().login(username, password, this);
    }
}
