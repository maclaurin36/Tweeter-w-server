package edu.byu.cs.tweeter.server.service.action.paged;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.UserDao;

public class FollowerPagedAction extends UserPagedAction {

    public FollowerPagedAction(AuthDao authDao, FollowDao followDao, UserDao userDao) {
        super(authDao, userDao, followDao);
    }

    @Override
    protected List<String> getUserAliases(PagedRequest<String> request) {
        return followDao.getFollowers(request.getAlias(), request.getLimit(), request.getLastItem());
    }
}
