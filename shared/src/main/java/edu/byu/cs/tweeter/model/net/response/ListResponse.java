package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

public class ListResponse<T> extends PagedResponse {

    private List<T> items;

    // Used for failure
    public ListResponse(String message) {
        super(false, message, false);
    }
}
