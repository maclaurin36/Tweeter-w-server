package edu.byu.cs.tweeter.client.presenter.view;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;

public interface PagedView<T> extends BaseView {
    void addItems(List<T> items);
    void goToUserView(User user);
    void setIsLoading(boolean value);
}
