package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.StatusPagedResponse;

public interface StatusDao {
    List<Status> getFeed(PagedRequest<Status> request);
    List<Status> getStory(PagedRequest<Status> request);
    void insertStatusToStory(Status status);
    void insertStatusToFeed(String feedAlias, Status status);
}
