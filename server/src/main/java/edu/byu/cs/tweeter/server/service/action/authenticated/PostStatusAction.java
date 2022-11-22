package edu.byu.cs.tweeter.server.service.action.authenticated;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.AuthDao;
import edu.byu.cs.tweeter.server.dao.FeedDao;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.StoryDao;

public class PostStatusAction extends AuthenticatedAction {
    private StoryDao storyDao;
    private FollowDao followDao;
    private FeedDao feedDao;
    private PostStatusRequest request;

    public PostStatusAction(AuthDao authDao, StoryDao storyDao, FollowDao followDao, FeedDao feedDao, PostStatusRequest request) {
        super(authDao, request);
        this.storyDao = storyDao;
        this.followDao = followDao;
        this.feedDao = feedDao;
        this.request = request;
        validate();
    }

    public Response postStatus() {
        storyDao.insertStatusToStory(request.getStatus());
        List<String> followers = followDao.getFollowers(request.getStatus().getUser().getAlias(), 100, null);
        for (String follower : followers) {
            feedDao.insertStatusToFeed(follower, request.getStatus());
        }
        return new Response(true);
    }

    private void validate() {
        if (request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Request needs to have a status");
        }
    }
}
