package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected PagedResponse<Status> getListResponse() throws IOException, TweeterRemoteException {
        String targetUserAlias = targetUser == null ? null : targetUser.getAlias();
        PagedRequest<Status> request = new PagedRequest<>(authToken, targetUserAlias, limit, lastItem);
        return getServerFacade().getStatusList(request, StatusService.GET_STORY_URL_PATH);
    }

//    @Override
//    protected Pair<List<Status>, Boolean> getItems() {
////        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
//        return new Pair<List<Status>, Boolean>(new ArrayList<>(), true);
//    }
}

// TODO implement lambda stuff