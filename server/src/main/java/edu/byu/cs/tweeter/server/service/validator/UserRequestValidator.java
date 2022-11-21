package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class UserRequestValidator extends AuthenticatedValidator {
    private UserRequest request;

    public UserRequestValidator(UserRequest request, UserDao userDao) {
        super(request, userDao);
        this.request = request;
    }

    @Override
    protected void validateRequest() {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
    }
}
