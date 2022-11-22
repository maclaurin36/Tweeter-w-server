package edu.byu.cs.tweeter.server.service.action.authenticated.paged;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class FollowingPagedAction extends UserPagedAction {

    public FollowingPagedAction(AuthDao authDao, FollowDao followDao, UserDao userDao, PagedRequest<String> request) {
        super(authDao, userDao, followDao, request);
    }

    @Override
    protected List<String> getUserAliases(PagedRequest<String> request) {
        return followDao.getFollowing(request);
    }
}
