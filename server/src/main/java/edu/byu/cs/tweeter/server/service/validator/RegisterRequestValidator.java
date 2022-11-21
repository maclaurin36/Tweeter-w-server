package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.net.request.RegisterRequest;

public class RegisterRequestValidator implements ValidatorTemplate {
    private RegisterRequest request;

    public RegisterRequestValidator(RegisterRequest request) {
        this.request = request;
    }

    @Override
    public void validate() {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a password");
        } else if (request.getFirstname() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a first name");
        } else if (request.getLastname() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a last name");
        } else if (request.getImage() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an image");
        }
    }
}
