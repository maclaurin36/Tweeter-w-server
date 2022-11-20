package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class FollowUnfollowRequest extends AuthenticatedRequest {
    private String userAlias;
    private String userToFollowUnfollowAlias;

    public FollowUnfollowRequest(AuthToken authToken, String userAlias, String userToFollowUnfollowAlias) {
        super(authToken);
        this.userAlias = userAlias;
        this.userToFollowUnfollowAlias = userToFollowUnfollowAlias;
    }

    public FollowUnfollowRequest() { super(null); }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserToFollowUnfollowAlias() {
        return userToFollowUnfollowAlias;
    }

    public void setUserToFollowUnfollowAlias(String userToFollowUnfollowAlias) {
        this.userToFollowUnfollowAlias = userToFollowUnfollowAlias;
    }
}
