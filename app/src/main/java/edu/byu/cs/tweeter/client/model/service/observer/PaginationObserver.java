package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface PaginationObserver<T> extends ServiceObserver {
    void handleGetListSuccess(List<T> items, Boolean hasMorePages);
    void handlePaginationFailure();
}
