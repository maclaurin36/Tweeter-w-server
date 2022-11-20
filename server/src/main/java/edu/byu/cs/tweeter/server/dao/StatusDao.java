package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.StatusPagedResponse;

public interface StatusDao {
    StatusPagedResponse getFeed(PagedRequest<Status> request);
    StatusPagedResponse getStory(PagedRequest<Status> request);
    Boolean postStatus(PostStatusRequest request);
}
