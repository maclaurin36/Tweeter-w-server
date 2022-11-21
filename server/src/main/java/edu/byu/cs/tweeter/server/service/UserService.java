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
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.utility.Authenticator;
import edu.byu.cs.tweeter.server.service.utility.HashUtility;
import edu.byu.cs.tweeter.server.service.validator.AuthenticatedValidator;
import edu.byu.cs.tweeter.server.service.validator.LoginValidator;
import edu.byu.cs.tweeter.server.service.validator.RegisterRequestValidator;
import edu.byu.cs.tweeter.server.service.validator.UserRequestValidator;

public class UserService extends BaseService {

    private final Integer initialFollowerCount = 0;
    private final Integer initialFollowingCount = 0;
    public UserService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public AuthenticateResponse login(LoginRequest request) {
        LoginValidator loginValidator = new LoginValidator(request);
        loginValidator.validate();

        FullUser userWithPassword = daoFactory.getUserDao().getUser(request.getUsername());

        if (userWithPassword == null) {
            throw new RuntimeException("[Bad Request] User with given alias not found");
        }

        try {
            if (!HashUtility.validatePassword(request.getPassword(), userWithPassword.getPassword())) {
                throw new RuntimeException("[Unauthorized] Given password did not match password in the database");
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("[Bad Request] Invalid password specified", ex);
        }

        AuthToken authToken = Authenticator.generateAuthToken();

        daoFactory.getUserDao().insertAuthToken(authToken);
        User user = new User(userWithPassword.getFirstName(), userWithPassword.getLastName(), userWithPassword.getAlias(), userWithPassword.getImageUrl());
        return new AuthenticateResponse(user, authToken);
    }

    public UserResponse getUser(UserRequest request) {
        UserRequestValidator userRequestValidator = new UserRequestValidator(request, daoFactory.getUserDao());
        userRequestValidator.validate();
        FullUser fullUser = daoFactory.getUserDao().getUser(request.getAlias());
        User user = new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl());
        return new UserResponse(user);
    }

    public Response logout(AuthenticatedRequest request) {
        AuthenticatedValidator authenticatedValidator = new AuthenticatedValidator(request, daoFactory.getUserDao());
        authenticatedValidator.validate();
        Boolean deleteSucceeded = daoFactory.getUserDao().deleteAuthToken(request.getAuthToken());
        return new Response(deleteSucceeded);
    }

    public AuthenticateResponse register(RegisterRequest request) {
        RegisterRequestValidator registerRequestValidator = new RegisterRequestValidator(request);
        registerRequestValidator.validate();
        String imageUrl = daoFactory.getImageDao().storeImage(request.getImage());
        User user = new User(request.getFirstname(), request.getLastname(), request.getAlias(), imageUrl);
        AuthToken authToken = Authenticator.generateAuthToken();

        String password;
        try {
            password = HashUtility.generateStrongPasswordHash(request.getPassword());
        }
        catch (Exception ex) {
            throw new RuntimeException("[Bad Request] Password hashing failed", ex);
        }
        FullUser fullUser = new FullUser(password, user.getFirstName(), user.getLastName(), user.getAlias(), imageUrl, initialFollowerCount, initialFollowingCount);
        daoFactory.getUserDao().insertUser(fullUser);
        daoFactory.getUserDao().insertAuthToken(authToken);
        return new AuthenticateResponse(user, authToken);
    }
}
