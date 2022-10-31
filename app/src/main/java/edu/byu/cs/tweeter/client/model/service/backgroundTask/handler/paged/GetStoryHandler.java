package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged;

import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetStoryTask.
 */
public class GetStoryHandler extends PagedHandler<Status> {

    public GetStoryHandler(PaginationObserver<Status> observer) {
        super(observer);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to get story";
    }
}