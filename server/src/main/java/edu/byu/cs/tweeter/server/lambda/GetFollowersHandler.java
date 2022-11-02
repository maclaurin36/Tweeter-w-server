package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<FollowerRequest, PagedResponse<User>> {
    @Override
    public PagedResponse<User> handleRequest(FollowerRequest request, Context context) {
        FollowService service = new FollowService();
        return service.getFollowers(request);
    }
}
