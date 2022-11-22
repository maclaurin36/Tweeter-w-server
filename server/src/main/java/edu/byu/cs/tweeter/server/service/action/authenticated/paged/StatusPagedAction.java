package edu.byu.cs.tweeter.server.service.action.authenticated.paged;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.PagedDao;

public class StatusPagedAction extends PagedAction<Status, Status> {
    private PagedDao<Status, Status> pagedDao;

    public StatusPagedAction(AuthDao authDao, PagedDao<Status, Status> pagedDao, PagedRequest<Status> request) {
        super(authDao, request);
        this.pagedDao = pagedDao;
    }

    @Override
    protected List<Status> getPage() {
        return pagedDao.getPage(request);
    }
}
