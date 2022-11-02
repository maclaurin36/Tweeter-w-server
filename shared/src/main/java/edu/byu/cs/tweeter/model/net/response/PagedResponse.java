package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

public class PagedResponse<T> extends Response {

    private final boolean hasMorePages;
    private List<T> items;

    // Error response
    PagedResponse(String message) {
        super(false, message);
        this.hasMorePages = false;
    }

    // Normal response
    public PagedResponse(boolean success, boolean hasMorePages, List<T> items) {
        super(success);
        this.hasMorePages = hasMorePages;
        this.items = items;
    }

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public List<T> getItems() {
        return this.items;
    }
}
