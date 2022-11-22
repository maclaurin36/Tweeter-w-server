package edu.byu.cs.tweeter.server.service.action.count;

import edu.byu.cs.tweeter.server.dao.dto.FullUser;
import edu.byu.cs.tweeter.server.service.action.count.CountAction;

public class FollowerCountAction extends CountAction {
    @Override
    protected int getSpecificCount(FullUser user) {
        return user.getFollowerCount();
    }
}
