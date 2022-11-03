package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService extends BaseService {

    public AuthenticateResponse login(LoginRequest request) {
        RequestValidator.validateLoginRequest(request);

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getDummyUser();
        AuthToken authToken = getDummyAuthToken();
        return new AuthenticateResponse(user, authToken);
    }

    public UserResponse getUser(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        UserDAO userDAO = getUserDao();
        return new UserResponse(userDAO.getUser(request.getAlias()));
    }

    public Response logout(AuthenticatedRequest request) {
        RequestValidator.validateAuthenticatedRequest(request);
        return new Response(true);
    }

    public AuthenticateResponse register(RegisterRequest request) {
        RequestValidator.validateRegisterRequest(request);
//        User user = new User(request.getFirstname(),request.getLastname(),request.getAlias(), request.getImage());
//        AuthToken authToken = new AuthToken();
        return new AuthenticateResponse(getDummyUser(), getDummyAuthToken());
    }


    User getDummyUser() {
        return getFakeData().getFirstUser();
    }
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }
    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
