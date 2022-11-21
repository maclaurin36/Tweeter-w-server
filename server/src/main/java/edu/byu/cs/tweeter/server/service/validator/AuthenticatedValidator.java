package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.service.utility.DateUtility;

public class AuthenticatedValidator implements ValidatorTemplate {
    private final AuthenticatedRequest request;
    private final UserDao userDao;
    private static final int SECONDS_UNTIL_EXPIRATION = 60;
    public AuthenticatedValidator(AuthenticatedRequest request, UserDao userDao) {
        this.request = request;
        this.userDao = userDao;
    }

    @Override
    public final void validate() {
        AuthToken storedAuthToken = userDao.getAuthToken(request.getAuthToken());
        if (storedAuthToken == null) {
            throw new RuntimeException("[Unauthorized] Request needs to have an authentication token");
        }
        if (DateUtility.getDate() > storedAuthToken.getExpiration()) {
            throw new RuntimeException("[Unauthorized] Invalid Auth Token");
        }
        refreshToken(storedAuthToken);
        validateRequest();
    }

    protected void validateRequest() {};

    private void refreshToken(AuthToken authToken) {
        AuthToken refreshedToken = new AuthToken(authToken.getToken(), authToken.getExpiration() + SECONDS_UNTIL_EXPIRATION);
        userDao.updateAuthToken(refreshedToken);
    }
}
