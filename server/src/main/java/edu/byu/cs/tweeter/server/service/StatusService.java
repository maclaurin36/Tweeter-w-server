package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

public class StatusService {
    public PagedResponse<Status> getFeed(PagedRequest request) {
        return null;
    }

    public PagedResponse<Status> getStory(PagedRequest request) {
        return null;
    }

    public Response postStatus(PostStatusRequest request) {
        return null;
    }
}
