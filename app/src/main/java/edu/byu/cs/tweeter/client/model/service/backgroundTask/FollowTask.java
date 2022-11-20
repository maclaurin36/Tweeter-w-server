package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowUnfollowRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    /**
     * The user that is being followed.
     */
    private final User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        String currentUserAlias = Cache.getInstance().getCurrUser().getAlias();
        FollowUnfollowRequest followRequest = new FollowUnfollowRequest(authToken, currentUserAlias, followee.getAlias());
        Response followResponse = getServerFacade().followUnfollow(followRequest, FollowService.FOLLOW_URL_PATH);
        if (followResponse.isSuccess()) {
            sendSuccessMessage();
        } else {
            sendFailedMessage(followResponse.getMessage());
        }
    }

}