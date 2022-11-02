package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PagedRequest<T> extends AuthenticatedRequest {

    private String currentUserAlias;
    private int limit;
    private T lastItem;

    public String getCurrentUserAlias() {
        return currentUserAlias;
    }

    public void setCurrentUserAlias(String currentUserAlias) {
        this.currentUserAlias = currentUserAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public T getLastItem() {
        return lastItem;
    }

    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
    }

    public PagedRequest(AuthToken authToken, String currentUserAlias, int limit, T lastItem) {
        super(authToken);
        this.currentUserAlias = currentUserAlias;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    // For Json
    private PagedRequest() {
        super(null);
    }

}
