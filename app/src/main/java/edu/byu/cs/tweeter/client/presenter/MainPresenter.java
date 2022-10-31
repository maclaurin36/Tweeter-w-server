package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.FollowerCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.FollowingCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.LogoutObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PostStatusObserver;
import edu.byu.cs.tweeter.client.model.service.observer.ToggleFollowObserver;
import edu.byu.cs.tweeter.client.presenter.view.MainActivityView;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends BasePresenter<MainActivityView> implements FollowingCountObserver, FollowerCountObserver, IsFollowerObserver, ToggleFollowObserver, PostStatusObserver, LogoutObserver {

    private static final String LOG_TAG = "MainPresenter";
    private final User selectedUser;
    private final FollowService followService;
    private final UserService userService;
    private StatusService statusService;

    public MainPresenter(MainActivityView view, User user) {
        super(view);
        followService = new FollowService();
        userService = new UserService();
        if (user == null) {
            throw new RuntimeException("User not passed to activity");
        }
        this.selectedUser = user;
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService =  new StatusService();
        }
        return statusService;
    }

    // region Task Callers
    public void getFollowingCount(User user) {
        followService.getFollowingCount(user, this);
    }

    public void getFollowersCount(User user) {
        followService.getFollowerCount(user, this);
    }

    public void getIsFollowing(User user) {
        followService.getIsFollower(user, this);
    }

    public void follow(User user) {
        view.displayInfoMessage("Adding " + selectedUser.getName() + "...");
        followService.follow(user, this);
    }

    public void unfollow(User user) {
        view.displayInfoMessage("Removing " + selectedUser.getName() + "...");
        followService.unfollow(user, this);
    }

    public void postStatus(String post) {
        displayInfoMessage("Posting Status...");
        try {
            getStatusService().postStatus(createStatusFromPost(post), this);
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            displayErrorMessage("Failed to post the status because of exception: " + ex.getMessage());
        }
    }

    public Status createStatusFromPost(String post) throws ParseException {
        return new Status(post, selectedUser, getFormattedDateTime(), parseURLs(post), parseMentions(post));
    }

    public void logout() {
        view.displayInfoMessage("Logging Out...");
        userService.logout(this);
    }
    // endregion

    //region Service Handlers

    @Override
    public void handleGetFollowingCountSucceeded(int count) {
        view.setFollowingCount(count);
    }

    @Override
    public void handleGetFollowerCountSucceeded(int count) {
        view.setFollowerCount(count);
    }

    @Override
    public void handleIsFollowerSucceeded(boolean isFollower) {
        if (isFollower) {
            view.setIsFollowerTrue();
        } else {
            view.setIsFollowerFalse();
        }
    }

    @Override
    public void handleToggleFollowSucceeded(Boolean value) {
        updateSelectedUserFollowingAndFollowers();
        updateFollowButton(value);
        view.enableUnfollowAndFollowing(true);
    }

    @Override
    public void handleToggleFollowFailed() {
        updateSelectedUserFollowingAndFollowers();
        view.enableUnfollowAndFollowing(true);
    }

    @Override
    public void handlePostStatusSuccess() {
        displayInfoMessage("Successfully Posted!");
    }

    @Override
    public void handlePostStatusException(Exception ex) {
        displayErrorMessage("Failed to post status with exception: " + ex.getMessage());
    }

    @Override
    public void handleFailure(String message) {
        displayErrorMessage("Failed to post status: " + message);
    }

    @Override
    public void handleLogoutSuccess() {
        logoutUser();
    }
    // endregion

    private void displayErrorMessage(String message) {
        view.clearErrorMessage();
        view.displayErrorMessage(message);
    }

    private void displayInfoMessage(String message) {
        view.clearInfoMessage();
        view.displayInfoMessage(message);
    }

    //region Validation Functions
    public void updateSelectedUserFollowingAndFollowers() {
        // Get count of most recently selected user's followers.
        getFollowersCount(selectedUser);

        // Get count of most recently selected user's followees (who they are following)
        getFollowingCount(selectedUser);
    }

    public void updateFollowButton(boolean removed) {
        // If follow relationship was removed.
        if (removed) {
            view.setIsFollowerFalse();
        } else {
            view.setIsFollowerTrue();
        }
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public void logoutUser() {
        //Clear user data (cached data).
        Cache.getInstance().clearCache();
        view.moveToLoginPage();
    }

    public void followButtonClick(String buttonMessage) {
        view.enableUnfollowAndFollowing(false);
        if (buttonMessage.equals("Following")) {
            unfollow(selectedUser);
        } else {
            follow(selectedUser);
        }
    }

    public void toggleFollowVisibility() {
        if (selectedUser.compareTo(Cache.getInstance().getCurrUser()) == 0) {
            view.setFollowButtonGone();
        } else {
            view.setFollowButtonVisible();
            getIsFollowing(selectedUser);
        }
    }
    //endregion
}
