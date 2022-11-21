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
        return daoFactory.getStatusDao().getStory(request);
    }

    public Response postStatus(PostStatusRequest request) {
        RequestValidator.validatePostStatusRequest(request);
        Boolean statusInserted = daoFactory.getStatusDao().postStatus(request);
        return new Response(statusInserted);
    }
}
