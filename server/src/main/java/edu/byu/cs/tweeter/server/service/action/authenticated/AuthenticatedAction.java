package edu.byu.cs.tweeter.server.service.action.authenticated;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.service.utility.AuthTokenGenerator;
import edu.byu.cs.tweeter.server.service.utility.DateUtility;

public class AuthenticatedAction {
    protected final AuthDao authDao;

    public AuthenticatedAction(AuthDao authDao, AuthenticatedRequest request) {
        this.authDao = authDao;

        validate(request);
    }

    private void validate(AuthenticatedRequest request) {
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
    }

    private void refreshToken(AuthToken authToken) {
        AuthToken refreshedToken = AuthTokenGenerator.generateAuthToken(authToken.getToken());
        authDao.insertAuthToken(refreshedToken);
    }
}
