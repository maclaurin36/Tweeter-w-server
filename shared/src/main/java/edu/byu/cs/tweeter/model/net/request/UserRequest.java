package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UserRequest extends AuthenticatedRequest {
    private String alias;

    public UserRequest(AuthToken authToken, String alias) {
        super(authToken);
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    private UserRequest() {super(null);}
}
