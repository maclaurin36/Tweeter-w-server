package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged;

import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Message handler (i.e., observer) for GetFollowersTask.
 */
public class GetFollowersHandler extends PagedHandler<User> {

    public GetFollowersHandler(PaginationObserver<User> observer) {
        super(observer);
    }

    @Override
    protected String getFailureMessage() {
        return "Failed to get followers";
    }
}