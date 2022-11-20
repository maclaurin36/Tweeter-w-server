package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.AwsDaoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class FollowHandler implements RequestHandler<FollowUnfollowRequest, Response> {
    @Override
    public Response handleRequest(FollowUnfollowRequest request, Context context) {
        DaoFactory daoFactory = new AwsDaoFactory();
        return new FollowService(daoFactory).follow(request);
    }
}
