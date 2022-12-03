package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.DaoFactory;
import edu.byu.cs.tweeter.server.service.action.BatchPostStatusAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.PostStatusAction;
import edu.byu.cs.tweeter.server.service.action.authenticated.paged.StatusPagedAction;

public class StatusService extends BaseService {

    public StatusService(DaoFactory daoFactory) {
        super(daoFactory);
    }

    public PagedResponse<Status> getFeed(PagedRequest<Status> request) {
        return new StatusPagedAction(daoFactory.getAuthDao(), daoFactory.getFeedDao(), request).getList(request);
    }

    public PagedResponse<Status> getStory(PagedRequest<Status> request) {
        return new StatusPagedAction(daoFactory.getAuthDao(), daoFactory.getStoryDao(), request).getList(request);
    }

    public Response postStatus(PostStatusRequest request) {
        return new PostStatusAction(daoFactory.getAuthDao(), daoFactory.getStoryDao(), daoFactory.getFollowDao(), daoFactory.getFeedDao(), daoFactory.getQueueDao(), request).postStatus();
    }

    public void batchPostStatus(SQSEvent batchedJob) {
        new BatchPostStatusAction(daoFactory.getFeedDao()).run(batchedJob);
    }
}
