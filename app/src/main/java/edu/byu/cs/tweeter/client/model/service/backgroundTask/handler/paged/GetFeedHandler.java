package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged;

import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetFeedTask.
 */
public class GetFeedHandler extends PagedHandler<Status>{

    public GetFeedHandler(PaginationObserver<Status> observer) {
        super(observer);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to get feed";
    }
}