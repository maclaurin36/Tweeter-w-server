package edu.byu.cs.tweeter.server.dao;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;

public interface PagedDao<T, U> {
    List<U> getPage(PagedRequest<T> request);
}
