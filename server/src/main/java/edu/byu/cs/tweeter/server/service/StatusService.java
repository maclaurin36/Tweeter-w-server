package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.model.net.response.StatusPagedResponse;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.service.utility.RequestValidator;

public class StatusService extends BaseService {

    public StatusService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public StatusPagedResponse getFeed(PagedRequest<Status> request) {
        RequestValidator.validatePagedRequest(request);
        List<Status> feedPage = daoFactory.getStatusDao().getFeed(request);
        return new StatusPagedResponse(true, !(feedPage.size() < request.getLimit()), feedPage);
    }

    public PagedResponse<Status> getStory(PagedRequest<Status> request) {
        RequestValidator.validatePagedRequest(request);
        List<Status> storyPage = daoFactory.getStatusDao().getStory(request);
        return new StatusPagedResponse(true, !(storyPage.size() < request.getLimit()), storyPage);
    }

    public Response postStatus(PostStatusRequest request) {
        RequestValidator.validatePostStatusRequest(request);
        daoFactory.getStatusDao().insertStatusToStory(request.getStatus());
        List<String> followers = daoFactory.getFollowDao().getFollowers(request.getStatus().getUser().getAlias(), 100, null);
        for (String follower : followers) {
            daoFactory.getStatusDao().insertStatusToFeed(follower, request.getStatus());
        }
        return new Response(true);
    }
}
