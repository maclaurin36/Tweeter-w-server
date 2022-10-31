package edu.byu.cs.tweeter.client.presenter.authenticate;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.view.AuthenticateView;

public class RegisterPresenter extends AuthenticatePresenter {

    public RegisterPresenter(AuthenticateView view) {
        super(view);
    }

    public void registerUser(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        String errorMessage = validateRegistration(firstName, lastName, alias, password, imageBytesBase64);
        boolean errorOccurred = handleErrorMessage(errorMessage);
        if (errorOccurred) {
            return;
        }

        view.displayInfoMessage("Registering...");
        // TODO uncomment this
//        new UserService().registerUser(firstName, lastName, alias, password, imageBytesBase64, this);
    }

    public String validateRegistration(String firstName, String lastName, String alias, String password, String imageToUpload) {
        if (firstName.length() == 0) {
            return "First Name cannot be empty.";
        }
        if (lastName.length() == 0) {
            return "Last Name cannot be empty.";
        }
        if (imageToUpload == null) {
            return "Profile image must be uploaded.";
        }
        return validateLoginCredentials(alias, password);
    }
}
