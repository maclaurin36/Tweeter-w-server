package edu.byu.cs.tweeter.client.presenter.view;

public interface MainActivityView extends BaseView {
    void setFollowingCount(int count);

    void setFollowerCount(int count);

    void setIsFollowerTrue();

    void setIsFollowerFalse();

    void enableUnfollowAndFollowing(boolean value);

    void moveToLoginPage();

    void setFollowButtonVisible();

    void setFollowButtonGone();

    void clearInfoMessage();

    void clearErrorMessage();
}
