package edu.byu.cs.tweeter.client.presenter.paged;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.GetUserObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.client.presenter.BasePresenter;
import edu.byu.cs.tweeter.client.presenter.view.PagedView;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<ListItemType>
        extends BasePresenter<PagedView<ListItemType>>
        implements PaginationObserver<ListItemType>, GetUserObserver {

    protected ListItemType lastItem;
    private boolean hasMorePages;
    private boolean isLoading;
    protected User currentUser;

    public PagedPresenter(PagedView<ListItemType> view, User user) {
        super(view);
        this.currentUser = user;
    }

    public void getListItems() {
        if (!isLoading) {
            isLoading = true;
            view.setIsLoading(true);
            getItemFromService();
        }
    }

    public void getUser(String username) {
        view.displayInfoMessage("Getting user's profile...");
        new UserService().getUser(username, this);
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    protected abstract void getItemFromService();

    @Override
    public void handleGetListSuccess(List<ListItemType> items, Boolean hasMorePages) {
        isLoading = false;
        view.setIsLoading(false);
        this.hasMorePages = hasMorePages;
        lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
        view.addItems(items);
    }

    @Override
    public void handlePaginationFailure() {
        isLoading = false;
        view.setIsLoading(false);
    }

    @Override
    public void handleGetUserSucceeded(User user) {
        view.goToUserView(user);
    }
}
