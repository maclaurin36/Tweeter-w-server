package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.service.UserService;

/**
 * An AWS lambda function that logs a user in and returns the user object and an auth code for
 * a successful login.
 */
public class LoginHandler implements RequestHandler<LoginRequest, AuthenticateResponse> {
    @Override
    public AuthenticateResponse handleRequest(LoginRequest loginRequest, Context context) {
        DaoFactory daoFactory = new DynamoDaoFactory();
        return new UserService(daoFactory).login(loginRequest);
    }
}
