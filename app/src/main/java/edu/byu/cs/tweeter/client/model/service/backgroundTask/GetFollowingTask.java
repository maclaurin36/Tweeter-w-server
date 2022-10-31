package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask<FollowingResponse> {

    private static final String LOG_TAG = "GetFollowingTask";

    private ServerFacade serverFacade;

    public GetFollowingTask(FollowService followService, AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected FollowingResponse getListResponse() throws IOException, TweeterRemoteException {
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        String lastAlias = lastItem == null ? null : lastItem.getAlias();
        FollowingRequest request = new FollowingRequest(authToken, targetUserAlias, limit, lastAlias);
        return getServerFacade().getFollowees(request, FollowService.URL_PATH);
    }

    @Override
    protected void setItems(FollowingResponse response) {
        this.items = response.getFollowees();
        this.hasMorePages = response.getHasMorePages();
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        if(serverFacade == null) {
            serverFacade = new ServerFacade();
        }

        return new ServerFacade();
    }
}
