package edu.byu.cs.tweeter.server.service.action.authenticated.count;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.action.authenticated.AuthenticatedAction;

public abstract class CountAction extends AuthenticatedAction {
    private UserDao userDao;
    private UserRequest request;

    public CountAction(UserDao userDao, AuthDao authDao, UserRequest userRequest) {
        super(authDao, userRequest);
        this.userDao = userDao;
        this.request = userRequest;

        validate();
    }

    public CountResponse getCount() {
        FullUser user = userDao.getUser(request.getAlias());
        int count = getSpecificCount(user);
        return new CountResponse(count);
    }

    private void validate() {
        if (request.getAlias() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have an alias");
        }
    }

    protected abstract int getSpecificCount(FullUser user);
}
