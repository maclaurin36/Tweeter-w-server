package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;
import edu.byu.cs.tweeter.client.presenter.view.BaseView;

public abstract class BasePresenter<T extends BaseView> implements ServiceObserver {
    protected T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    @Override
    public void handleFailure(String message) {
        view.displayErrorMessage(message);
    }
}
