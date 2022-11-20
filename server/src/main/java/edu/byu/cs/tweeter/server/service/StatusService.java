package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DaoFactory;

public class StatusService extends BaseService {

    public StatusService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public PagedResponse<Status> getFeed(PagedRequest<Status> request) {
        RequestValidator.validatePagedRequest(request);
        return daoFactory.getStatusDao().getFeed(request);
    }

    public PagedResponse<Status> getStory(PagedRequest<Status> request) {
        RequestValidator.validatePagedRequest(request);
        return daoFactory.getStatusDao().getStory(request);
    }

    public Response postStatus(PostStatusRequest request) {
        RequestValidator.validatePostStatusRequest(request);
        Boolean statusInserted = daoFactory.getStatusDao().postStatus(request);
        return new Response(statusInserted);
    }
}
