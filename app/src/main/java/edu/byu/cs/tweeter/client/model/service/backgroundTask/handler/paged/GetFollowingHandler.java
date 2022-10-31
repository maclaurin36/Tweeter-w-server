package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged;

import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowingTask.
 */
public class GetFollowingHandler extends PagedHandler<User> {

    public GetFollowingHandler(PaginationObserver<User> observer) {
        super(observer);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to get following";
    }
}