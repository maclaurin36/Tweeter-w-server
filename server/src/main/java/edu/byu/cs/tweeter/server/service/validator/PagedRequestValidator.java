package edu.byu.cs.tweeter.server.service.validator;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class PagedRequestValidator<T> extends AuthenticatedValidator {
    private PagedRequest<T> request;

    public PagedRequestValidator(PagedRequest<T> request, UserDao userDao) {
        super(request, userDao);
        this.request = request;
    }

    @Override
    protected void validateRequest() {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[Bad Request] Request needs to have a positive limit");
        }
    }
}
