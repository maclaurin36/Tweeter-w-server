package edu.byu.cs.tweeter.client.presenter.paged;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    public FollowingPresenter(PagedView<User> view, User user) {
        super(view, user);
    }

    @Override
    protected void getItemFromService() {
        new FollowService().getFollowing(currentUser, lastItem, this);
    }
}
