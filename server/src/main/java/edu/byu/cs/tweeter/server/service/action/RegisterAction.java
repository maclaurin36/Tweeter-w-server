package edu.byu.cs.tweeter.server.service.action;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.ImageDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.utility.AuthTokenGenerator;
import edu.byu.cs.tweeter.server.service.utility.HashUtility;
import edu.byu.cs.tweeter.server.service.validator.RegisterRequestValidator;

public class RegisterAction extends AuthenticateAction {
    private RegisterRequest request;
    private ImageDao imageDao;
    private final Integer initialFollowerCount = 0;
    private final Integer initialFollowingCount = 0;

    public RegisterAction(UserDao userDao, AuthDao authDao, ImageDao imageDao, RegisterRequest request) {
        super(userDao, authDao, request);

        this.request = request;
        this.imageDao = imageDao;
        validate();
    }

    private void validate() {
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

    public AuthenticateResponse authenticate() {
        RegisterRequestValidator registerRequestValidator = new RegisterRequestValidator(request);
        registerRequestValidator.validate();
        String imageUrl = imageDao.storeImage(request.getImage());
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
        userDao.insertUser(fullUser);
        authDao.insertAuthToken(authToken);
        return new AuthenticateResponse(user, authToken);
    }
}
