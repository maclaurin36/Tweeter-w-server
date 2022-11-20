package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.dao.AwsDaoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetStoryHandler implements RequestHandler<PagedRequest<Status>, PagedResponse<Status>> {
    @Override
    public PagedResponse<Status> handleRequest(PagedRequest<Status> request, Context context) {
        DaoFactory daoFactory = new AwsDaoFactory();
        return new StatusService(daoFactory).getStory(request);
    }
}
