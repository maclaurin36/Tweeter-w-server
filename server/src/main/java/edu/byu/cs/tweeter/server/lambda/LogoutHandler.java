package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.AuthenticatedRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.AwsDaoFactory;
import edu.byu.cs.tweeter.server.service.UserService;

public class LogoutHandler implements RequestHandler<AuthenticatedRequest, Response> {
    @Override
    public Response handleRequest(AuthenticatedRequest request, Context context) {
        DaoFactory daoFactory = new AwsDaoFactory();
        return new UserService(daoFactory).logout(request);
    }
}
