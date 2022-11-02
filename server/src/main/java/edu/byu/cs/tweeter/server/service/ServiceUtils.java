package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;

public class ServiceUtils {
    public static void validatePagedRequest(PagedRequest request) {
        if(request.getCurrentUserAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
    }
}
