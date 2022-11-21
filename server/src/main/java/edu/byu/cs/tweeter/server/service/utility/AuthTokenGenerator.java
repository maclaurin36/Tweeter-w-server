package edu.byu.cs.tweeter.server.service.utility;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class AuthTokenGenerator {
    public static final int SECONDS_VALID = 300;

    public static AuthToken generateAuthToken() {
        String token = UUID.randomUUID().toString();
        return new AuthToken(token, DateUtility.getDate() + SECONDS_VALID);
    }

    public static AuthToken generateAuthToken(String token) {
        return new AuthToken(token, DateUtility.getDate() + SECONDS_VALID);
    }
}
