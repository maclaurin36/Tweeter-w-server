package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

public abstract class PagedUserTask extends PagedTask<User, PagedResponse<User>> {
    protected PagedUserTask(AuthToken authToken, User targetUser, int limit, User lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected final List<User> getUsersForItems(List<User> items) {
        return items;
    }

    @Override
    protected void setItems(PagedResponse<User> response) {
        this.items = response.getItems();
        this.hasMorePages = response.getHasMorePages();
    }
}
