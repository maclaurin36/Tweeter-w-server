package edu.byu.cs.tweeter.server.service;

import java.util.Date;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.UserResponse;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.Authentication;

public class UserService extends BaseService {

    public UserService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public AuthenticateResponse login(LoginRequest request) {
        RequestValidator.validateLoginRequest(request);

        FullUser userWithPassword = daoFactory.getUserDao().getUser(request.getUsername());

        if (userWithPassword == null) {
            throw new RuntimeException("[Bad Request] User with given alias not found");
        }

        if (!userWithPassword.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("[Unauthorized] Given password did not match password in the database");
        }

        AuthToken authToken = new AuthToken(UUID.randomUUID().toString(), new Date().getTime());

        daoFactory.getUserDao().insertAuthToken(userWithPassword.getAlias(), authToken);
        User user = new User(userWithPassword.getFirstName(), userWithPassword.getLastName(), userWithPassword.getAlias(), userWithPassword.getImageUrl());
        return new AuthenticateResponse(user, authToken);
    }

    public UserResponse getUser(UserRequest request) {
        RequestValidator.validateUserRequest(request);
        FullUser fullUser = daoFactory.getUserDao().getUser(request.getAlias());
        User user = new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl());
        return new UserResponse(user);
    }

    public Response logout(AuthenticatedRequest request) {
        RequestValidator.validateAuthenticatedRequest(request);

        return new Response(daoFactory.getUserDao().invalidateAuthToken(request.getAuthToken()));
    }

    public AuthenticateResponse register(RegisterRequest request) {
        RequestValidator.validateRegisterRequest(request);
        return daoFactory.getUserDao().register(request);
    }
}
