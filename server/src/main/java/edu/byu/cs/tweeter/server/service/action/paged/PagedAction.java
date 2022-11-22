package edu.byu.cs.tweeter.server.service.action.paged;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.PagedDao;
import edu.byu.cs.tweeter.server.service.validator.PagedRequestValidator;

public abstract class PagedAction<T, U> {
    private final AuthDao authDao;

    public PagedAction(AuthDao authDao) {
        this.authDao = authDao;
    }

    public PagedResponse<U> getList(PagedRequest<T> request) {
        PagedRequestValidator<T> pagedRequestValidator = new PagedRequestValidator<>(request, authDao);
        pagedRequestValidator.validate();
        List<U> page = getPage(request);
        boolean hasMorePages = !(page.size() < request.getLimit());
        return new PagedResponse<>(true, hasMorePages, page);
    }

    protected abstract List<U> getPage(PagedRequest<T> request);
}
