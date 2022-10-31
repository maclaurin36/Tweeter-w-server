package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.paged;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;

public abstract class PagedHandler<T> extends BackgroundTaskHandler<PaginationObserver<T>> {
    public PagedHandler(PaginationObserver<T> observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(Bundle data) {
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        Boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        observer.handleGetListSuccess(items, hasMorePages);
    }

    @Override
    protected void handleTaskFailure(String message) {
        observer.handlePaginationFailure();
        observer.handleFailure(message);
    }
}
