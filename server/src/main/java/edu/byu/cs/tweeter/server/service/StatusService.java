package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {
    public PagedResponse<Status> getFeed(PagedRequest<Status> request) {
        RequestValidator.validatePagedRequest(request);
        FakeData fakeData = FakeData.getInstance();
        Pair<List<Status>, Boolean> statusResults = fakeData.getPageOfStatus(request.getLastItem(), request.getLimit());
        return new PagedResponse<Status>(true, statusResults.getSecond(), statusResults.getFirst());
    }

    public PagedResponse<Status> getStory(PagedRequest<Status> request) {
        RequestValidator.validatePagedRequest(request);
        FakeData fakeData = FakeData.getInstance();
        Pair<List<Status>, Boolean> statusResults = fakeData.getPageOfStatus(request.getLastItem(), request.getLimit());
        return new PagedResponse<Status>(true, statusResults.getSecond(), statusResults.getFirst());
    }

    public Response postStatus(PostStatusRequest request) {
        RequestValidator.validatePostStatusRequest(request);
        return new Response(true);
    }
}
