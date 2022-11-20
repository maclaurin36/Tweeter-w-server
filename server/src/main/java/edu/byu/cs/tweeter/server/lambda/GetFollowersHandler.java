package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.AwsDaoFactory;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<PagedRequest<String>, PagedResponse<User>> {
    @Override
    public PagedResponse<User> handleRequest(PagedRequest<String> request, Context context) {
        DaoFactory daoFactory = new AwsDaoFactory();
        return new FollowService(daoFactory).getFollowers(request);
    }
}
