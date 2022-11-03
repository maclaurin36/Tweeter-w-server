package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class IsFollowerRequest extends AuthenticatedRequest {
    private String followerAlias;
    private String followeeAlias;

    public IsFollowerRequest(AuthToken authToken, String followerAlias, String followeeAlias) {
        super(authToken);
        this.followeeAlias = followeeAlias;
        this.followerAlias = followerAlias;
    }

    private IsFollowerRequest() { super(null); }

    public String getFollowerAlias() {
        return followerAlias;
    }

    public void setFollowerAlias(String followerAlias) {
        this.followerAlias = followerAlias;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }
}
