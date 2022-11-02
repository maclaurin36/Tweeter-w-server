package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;

public class RequestValidator {
    public static void validateAuthenticatedRequest(AuthenticatedRequest request) {
        if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an authentication token");
        }
    }

    public static void validateLoginRequest(LoginRequest loginRequest) {
        if (loginRequest.getUsername() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a username");
        } else if (loginRequest.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a password");
        }
    }

    public static void validatePagedRequest(PagedRequest request) {
        if (request.getCurrentUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
        validateAuthenticatedRequest(request);
    }

    public static void validatePostStatusRequest(PostStatusRequest request) {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
        validateAuthenticatedRequest(request);
    }

    public static void validateRegisterRequest(RegisterRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a username");
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

    public static void validateUserRequest(UserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a username");
        }
        validateAuthenticatedRequest(request);
    }
}
