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
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {

    /**
     * The user that is being followed.
     */
    private final User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        String curUserAlias = Cache.getInstance().getCurrUser().getAlias();
        FollowUnfollowRequest unfollowRequest = new FollowUnfollowRequest(authToken, curUserAlias, followee.getAlias());
        Response followResponse = getServerFacade().followUnfollow(unfollowRequest, FollowService.UNFOLLOW_URL_PATH);
        if (followResponse.isSuccess()) {
            sendSuccessMessage();
        } else {
            sendFailedMessage(followResponse.getMessage());
        }
    }


}

// TODO implement lambda stuff