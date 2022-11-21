package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.service.utility.AuthTokenGenerator;
import edu.byu.cs.tweeter.server.service.utility.DateUtility;

public class AuthenticatedValidator implements ValidatorTemplate {
    private final AuthenticatedRequest request;
    private final AuthDao authDao;
    public AuthenticatedValidator(AuthenticatedRequest request, AuthDao authDao) {
        this.request = request;
        this.authDao = authDao;
    }

    @Override
    public final void validate() {
        AuthToken storedAuthToken = authDao.getAuthToken(request.getAuthToken());
        if (storedAuthToken == null) {
            throw new RuntimeException("[Unauthorized] Request needs to have an authentication token");
        }
        if (DateUtility.getDate() > storedAuthToken.getExpiration()) {
            authDao.deleteAuthToken(storedAuthToken);
            throw new RuntimeException("[Unauthorized] Invalid Auth Token");
        }
        if (storedAuthToken.getExpiration() < DateUtility.getDate() + AuthTokenGenerator.SECONDS_VALID) {
            refreshToken(storedAuthToken);
        }
        validateRequest();
    }

    protected void validateRequest() {};

    private void refreshToken(AuthToken authToken) {
        AuthToken refreshedToken = AuthTokenGenerator.generateAuthToken(authToken.getToken());
        authDao.insertAuthToken(refreshedToken);
    }
}
