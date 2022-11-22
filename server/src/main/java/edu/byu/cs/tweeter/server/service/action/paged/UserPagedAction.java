package edu.byu.cs.tweeter.server.service.action.paged;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;

public abstract class UserPagedAction extends PagedAction<String, User> {
    private UserDao userDao;
    protected FollowDao followDao;

    public UserPagedAction(AuthDao authDao, UserDao userDao, FollowDao followDao) {
        super(authDao);
        this.userDao = userDao;
        this.followDao = followDao;
    }

    @Override
    protected final List<User> getPage(PagedRequest<String> request) {
        List<String> aliases = getUserAliases(request);
        List<FullUser> fullUsers = userDao.batchGetUser(aliases);
        List<User> users = fullUsers.stream().map(fullUser -> new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl())).collect(Collectors.toList());
        return users;
    }

    protected abstract List<String> getUserAliases(PagedRequest<String> request);
}
