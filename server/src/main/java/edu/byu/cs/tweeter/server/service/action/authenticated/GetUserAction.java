package edu.byu.cs.tweeter.server.service.action.authenticated;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;

public class GetUserAction extends AuthenticatedAction {
    private UserRequest userRequest;
    private UserDao userDao;

    public GetUserAction(AuthDao authDao, UserDao userDao, UserRequest request) {
        super(authDao, request);

        this.userRequest = request;
        this.userDao = userDao;
        validate();
    }

    private void validate() {
        if (userRequest.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
    }

    public UserResponse getUser() {
        FullUser fullUser = userDao.getUser(userRequest.getAlias());
        User user = new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl());
        return new UserResponse(user);
    }
}
