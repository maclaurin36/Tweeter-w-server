package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class PostStatusRequest extends AuthenticatedRequest {
    private final Status status;

    public PostStatusRequest(AuthToken authToken, Status status) {
        super(authToken);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
