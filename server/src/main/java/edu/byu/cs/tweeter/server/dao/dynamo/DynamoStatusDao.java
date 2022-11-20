package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.StatusPagedResponse;
import edu.byu.cs.tweeter.server.dao.StatusDao;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class DynamoStatusDao implements StatusDao {

    @Override
    public StatusPagedResponse getFeed(PagedRequest<Status> request) {
        return new StatusPagedResponse(true, true, new ArrayList<>());
//        FakeData fakeData = FakeData.getInstance();
//        Pair<List<Status>, Boolean> statusResults = fakeData.getPageOfStatus(request.getLastItem(), request.getLimit());
//        return new StatusPagedResponse(true, statusResults.getSecond(), statusResults.getFirst());
    }

    @Override
    public StatusPagedResponse getStory(PagedRequest<Status> request) {
        return new StatusPagedResponse(true, true, new ArrayList<>());
//        FakeData fakeData = FakeData.getInstance();
//        Pair<List<Status>, Boolean> statusResults = fakeData.getPageOfStatus(request.getLastItem(), request.getLimit());
//        return new StatusPagedResponse(true, statusResults.getSecond(), statusResults.getFirst());
    }

    @Override
    public Boolean postStatus(PostStatusRequest request) {
        return true;
    }
}
