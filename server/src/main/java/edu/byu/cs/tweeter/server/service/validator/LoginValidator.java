package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;

public class LoginValidator implements ValidatorTemplate {
    private LoginRequest loginRequest;

    public LoginValidator(LoginRequest request) {
        this.loginRequest = request;
    }

    @Override
    public void validate() {
        if (loginRequest.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a username");
        } else if (loginRequest.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a password");
        }
    }
}
