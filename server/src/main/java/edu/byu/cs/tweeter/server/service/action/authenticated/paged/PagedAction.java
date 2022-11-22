package edu.byu.cs.tweeter.server.service.action.authenticated.paged;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.service.action.authenticated.AuthenticatedAction;

public abstract class PagedAction<T, U> extends AuthenticatedAction {
    protected PagedRequest<T> request;
    public PagedAction(AuthDao authDao, PagedRequest<T> request) {
        super(authDao, request);
        this.request = request;

        validate();
    }

    public PagedResponse<U> getList(PagedRequest<T> request) {
        List<U> page = getPage();
        boolean hasMorePages = !(page.size() < request.getLimit());
        return new PagedResponse<>(true, hasMorePages, page);
    }

    private void validate() {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
    }

    protected abstract List<U> getPage();
}
