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
import edu.byu.cs.tweeter.server.service.utility.AuthTokenGenerator;
import edu.byu.cs.tweeter.server.service.utility.HashUtility;
import edu.byu.cs.tweeter.server.service.validator.LoginValidator;
import edu.byu.cs.tweeter.server.service.validator.RegisterRequestValidator;

public class UserService extends BaseService {

    private final Integer initialFollowerCount = 0;
    private final Integer initialFollowingCount = 0;
    public UserService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public AuthenticateResponse login(LoginRequest request) {
        LoginValidator loginValidator = new LoginValidator(request);
        loginValidator.validate();

        FullUser userWithPassword = daoFactory.getUserDao().getUser(request.getAlias());

        if (userWithPassword == null) {
            return new AuthenticateResponse("Invalid alias");
        }

        try {
            if (!HashUtility.validatePassword(request.getPassword(), userWithPassword.getPassword())) {
                return new AuthenticateResponse("Invalid password");
            }
        }
        catch (Exception ex) {
            throw new RuntimeException("[Bad Request] Invalid password specified", ex);
        }

        AuthToken authToken = AuthTokenGenerator.generateAuthToken();

        daoFactory.getAuthDao().insertAuthToken(authToken);
        User user = new User(userWithPassword.getFirstName(), userWithPassword.getLastName(), userWithPassword.getAlias(), userWithPassword.getImageUrl());
        return new AuthenticateResponse(user, authToken);
    }

    public UserResponse getUser(UserRequest request) {
        FullUser fullUser = daoFactory.getUserDao().getUser(request.getAlias());
        User user = new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl());
        return new UserResponse(user);
    }

    public Response logout(AuthenticatedRequest request) {
        Boolean deleteSucceeded = daoFactory.getAuthDao().deleteAuthToken(request.getAuthToken());
        return new Response(deleteSucceeded);
    }

    public AuthenticateResponse register(RegisterRequest request) {
        RegisterRequestValidator registerRequestValidator = new RegisterRequestValidator(request);
        registerRequestValidator.validate();
        String imageUrl = daoFactory.getImageDao().storeImage(request.getImage());
        User user = new User(request.getFirstname(), request.getLastname(), request.getAlias(), imageUrl);
        AuthToken authToken = AuthTokenGenerator.generateAuthToken();

        String password;
        try {
            password = HashUtility.generateStrongPasswordHash(request.getPassword());
        }
        catch (Exception ex) {
            throw new RuntimeException("[Bad Request] Password hashing failed", ex);
        }
        FullUser fullUser = new FullUser(password, user.getFirstName(), user.getLastName(), user.getAlias(), imageUrl, initialFollowerCount, initialFollowingCount);
        daoFactory.getUserDao().insertUser(fullUser);
        daoFactory.getAuthDao().insertAuthToken(authToken);
        return new AuthenticateResponse(user, authToken);
    }
}
