package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.response.Response;

public abstract class PagedTask<T, U extends Response> extends AuthenticatedTask {

    public static final String ITEMS_KEY = "items";
    public static final String MORE_PAGES_KEY = "more-pages";
    public static final String LOG_TAG = "paged-items";

    /**
     * The user whose items are being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    protected final User targetUser;

    /**
     * Maximum number of statuses to return (i.e., page size).
     */

    protected final int limit;

    /**
     * The last status returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    protected final T lastItem;

    /**
     * The items returned in the current page of results.
     */
    protected List<T> items;

    /**
     * Indicates whether there are more pages of items that can be retrieved on subsequent calls.
     */
    protected boolean hasMorePages;

    protected PagedTask(AuthToken authToken, User targetUser, int limit, T lastItem, Handler messageHandler) {
        super(authToken, messageHandler);
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastItem = lastItem;
    }

    protected User getTargetUser() {
        return targetUser;
    }

    protected int getLimit() {
        return limit;
    }

    protected T getLastItem() {
        return lastItem;
    }

    @Override
    protected final void runTask() {

        try {
            U response = getListResponse();
            if (response.isSuccess()) {
                setItems(response);
                sendSuccessMessage();
            } else {
                sendFailedMessage(response.getMessage());
            }
        } catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to get paged items", ex);
            sendExceptionMessage(ex);
        }
    }

    protected abstract List<User> getUsersForItems(List<T> items);

    protected abstract U getListResponse() throws IOException, TweeterRemoteException;

    protected abstract void setItems(U response);

    @Override
    protected final void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(ITEMS_KEY, (Serializable) items);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
}
