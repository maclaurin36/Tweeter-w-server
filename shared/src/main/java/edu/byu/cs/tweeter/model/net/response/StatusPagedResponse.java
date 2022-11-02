package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class StatusPagedResponse extends PagedResponse<Status> {
    public StatusPagedResponse(String message) {
        super(message);
    }

    public StatusPagedResponse(boolean success, boolean hasMorePages, List<Status> items) {
        super(success, hasMorePages, items);
    }
}
