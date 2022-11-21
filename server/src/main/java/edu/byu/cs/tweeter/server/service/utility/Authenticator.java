package edu.byu.cs.tweeter.server.service.utility;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class Authenticator {
    private static final int SECONDS_VALID = 60;

    public static AuthToken generateAuthToken() {
        String token = UUID.randomUUID().toString();
        return new AuthToken(token, DateUtility.getDate() + SECONDS_VALID);
    }

    public static Boolean checkAuthTokenValid(UserDao userDao, AuthToken authToken) {
        AuthToken storedAuthToken = userDao.getAuthToken(authToken);
        if (storedAuthToken == null) {
            return false;
        }
        return DateUtility.getDate() <= storedAuthToken.getExpiration();
    }

    public static AuthToken updateAuthToken(AuthToken authToken) {
        return new AuthToken(authToken.getToken(), DateUtility.getDate() + SECONDS_VALID);
    }
}
