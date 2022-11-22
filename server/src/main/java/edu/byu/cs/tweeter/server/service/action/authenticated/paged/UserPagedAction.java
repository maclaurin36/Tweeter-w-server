package edu.byu.cs.tweeter.server.service.action.authenticated.paged;

import java.util.ArrayList;
import java.util.Comparator;
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

    public UserPagedAction(AuthDao authDao, UserDao userDao, FollowDao followDao, PagedRequest<String> request) {
        super(authDao, request);
        this.userDao = userDao;
        this.followDao = followDao;
    }

    @Override
    protected final List<User> getPage() {
        List<String> aliases = getUserAliases(request);
        if (aliases.size() == 0) {
            return new ArrayList<>();
        }
        List<FullUser> fullUsers = userDao.batchGetUser(aliases);
        fullUsers.sort(Comparator.comparingInt(o -> aliases.indexOf(o.getAlias())));
        List<User> users = fullUsers.stream().map(fullUser -> new User(fullUser.getFirstName(), fullUser.getLastName(), fullUser.getAlias(), fullUser.getImageUrl())).collect(Collectors.toList());
        return users;
    }

    protected abstract List<String> getUserAliases(PagedRequest<String> request);
}
