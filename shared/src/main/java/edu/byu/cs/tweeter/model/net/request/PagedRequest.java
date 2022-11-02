package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PagedRequest<T> extends AuthenticatedRequest {

    private String alias;
    private int limit;
    private T lastItem;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public PagedRequest(AuthToken authToken, String alias, int limit, T lastItem) {
        super(authToken);
        this.alias = alias;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    // For Json
    private PagedRequest() {
        super(null);
    }

}
