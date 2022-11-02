package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected PagedResponse<User> getListResponse() throws IOException, TweeterRemoteException {
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        String lastAlias = lastItem == null ? null : lastItem.getAlias();
        PagedRequest<String> request = new PagedRequest<>(authToken, targetUserAlias, limit, lastAlias);
        return getServerFacade().getUserList(request, FollowService.GET_FOLLOWERS_URL_PATH);
    }
}