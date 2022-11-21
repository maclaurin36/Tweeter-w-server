package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;

public interface FeedDao {
    List<Status> getPage(PagedRequest<Status> request);
    void insertStatusToFeed(String alias, Status status);
}
