package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.service.action.LoginAction;
import edu.byu.cs.tweeter.server.service.action.RegisterAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.GetUserAction;

public class UserService extends BaseService {

    public UserService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public AuthenticateResponse login(LoginRequest request) {
        return new LoginAction(daoFactory.getUserDao(), daoFactory.getAuthDao(), request).authenticate();
    }

    public UserResponse getUser(UserRequest request) {
        return new GetUserAction(daoFactory.getAuthDao(), daoFactory.getUserDao(), request).getUser();
    }

    public Response logout(AuthenticatedRequest request) {
        Boolean deleteSucceeded = daoFactory.getAuthDao().deleteAuthToken(request.getAuthToken());
        return new Response(deleteSucceeded);
    }

    public AuthenticateResponse register(RegisterRequest request) {
        return new RegisterAction(daoFactory.getUserDao(), daoFactory.getAuthDao(), daoFactory.getImageDao(), request).authenticate();
    }
}
