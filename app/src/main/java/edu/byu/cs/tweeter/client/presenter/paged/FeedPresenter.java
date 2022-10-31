package edu.byu.cs.tweeter.client.presenter.paged;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    public FeedPresenter(PagedView<Status> view, User user) {
        super(view, user);
    }

    @Override
    protected void getItemFromService() {
        new StatusService().getFeed(currentUser, lastItem, this);
    }
}
