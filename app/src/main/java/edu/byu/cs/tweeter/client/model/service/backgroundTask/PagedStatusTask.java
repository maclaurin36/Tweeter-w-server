package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.util.List;
import java.util.stream.Collectors;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

public abstract class PagedStatusTask extends PagedTask<Status, PagedResponse<Status>> {

    protected PagedStatusTask(AuthToken authToken, User targetUser, int limit, Status lastItem, Handler messageHandler) {
        super(authToken, targetUser, limit, lastItem, messageHandler);
    }

    @Override
    protected final List<User> getUsersForItems(List<Status> items) {
        return items.stream().map(x -> x.user).collect(Collectors.toList());
    }

    @Override
    protected void setItems(PagedResponse<Status> response) {
        this.items = response.getItems();
        this.hasMorePages = response.getHasMorePages();
    }
}
