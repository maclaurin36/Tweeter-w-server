package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class PostStatusRequestValidator extends AuthenticatedValidator {
    private PostStatusRequest request;

    public PostStatusRequestValidator(PostStatusRequest request, AuthDao authDao) {
        super(request, authDao);
        this.request = request;
    }

    @Override
    protected void validateRequest() {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
    }
}
