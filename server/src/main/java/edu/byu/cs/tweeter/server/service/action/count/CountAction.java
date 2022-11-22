package edu.byu.cs.tweeter.server.service.action.count;

import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.UserDao;
import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.validator.UserRequestValidator;

public abstract class CountAction {
    public CountResponse getCount(UserRequest request, AuthDao authDao, UserDao userDao) {
        UserRequestValidator userRequestValidator = new UserRequestValidator(request, authDao);
        userRequestValidator.validate();
        FullUser user = userDao.getUser(request.getAlias());
        int count = getSpecificCount(user);
        return new CountResponse(count);
    }

    protected abstract int getSpecificCount(FullUser user);
}
