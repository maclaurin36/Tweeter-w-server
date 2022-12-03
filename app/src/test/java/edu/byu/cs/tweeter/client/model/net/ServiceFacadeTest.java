package edu.byu.cs.tweeter.client.model.net;

import android.os.Looper;
import android.util.Log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.view.MainActivityView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServiceFacadeTest {

    private final ServerFacade serverFacade = new ServerFacade();
    private final User testUser = FakeData.getInstance().getFirstUser();

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        RegisterRequest registerRequest = new RegisterRequest(testUser.getAlias(), testUser.getFirstName(), testUser.getLastName(), testUser.getImageUrl(), "password");
        AuthenticateResponse response = serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNull(response.getMessage());

        User returnedUser = response.getUser();
        Assertions.assertEquals(testUser, returnedUser);
        Assertions.assertNotNull(response.getAuthToken());
    }

    @Test
    public void testRegister_missingParameters_invalidRequestResponse() throws IOException, TweeterRemoteException {
        RegisterRequest registerRequest = new RegisterRequest(null, testUser.getFirstName(), testUser.getLastName(), testUser.getImageUrl(), "password");

        TweeterRequestException exception = Assertions.assertThrows(TweeterRequestException.class, () -> {
            serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH);
        });

        Assertions.assertEquals(exception.getMessage().substring(0, 13), "[Bad Request]");
    }

    @Test
    public void testGetFollowers_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        User testUser = FakeData.getInstance().getFirstUser();
        PagedRequest<String> pagedRequest = new PagedRequest<>(FakeData.getInstance().getAuthToken(), testUser.getAlias(), 3, null);
        PagedResponse<User> response = serverFacade.getUserList(pagedRequest, FollowService.GET_FOLLOWERS_URL_PATH);

        Assertions.assertTrue(response.isSuccess());
        List<User> returnedUsers = response.getItems();

        Assertions.assertTrue(response.getHasMorePages());
        Assertions.assertNotNull(response.getItems());
        List<User> fakeUsers = FakeData.getInstance().getFakeUsers().subList(0, 3);
        for (int i = 0; i < 3; i++) {
            Assertions.assertEquals(fakeUsers.get(i), returnedUsers.get(i));
        }
    }

    @Test
    public void testGetFollowers_missingAuthToken_unauthorizedResponse() {
        PagedRequest<String> pagedRequest = new PagedRequest<>(null, testUser.getAlias(), 3, null);

        TweeterRequestException exception = Assertions.assertThrows(TweeterRequestException.class, () -> {
            serverFacade.getUserList(pagedRequest, FollowService.GET_FOLLOWERS_URL_PATH);
        });

        Assertions.assertEquals("[Unauthorized]", exception.getMessage().substring(0, 14));
    }

    @Test
    public void testGetFollowers_missingParameters_invalidRequestResponse() {
        PagedRequest<String> pagedRequest = new PagedRequest<>(FakeData.getInstance().getAuthToken(), null, 3, null);

        TweeterRequestException exception = Assertions.assertThrows(TweeterRequestException.class, () -> {
            serverFacade.getUserList(pagedRequest, FollowService.GET_FOLLOWERS_URL_PATH);
        });

        Assertions.assertEquals(exception.getMessage().substring(0, 13), "[Bad Request]");
    }

    @Test
    public void testGetFollowerCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        assertCountRequestResponse(FollowService.GET_FOLLOWER_COUNT_URL_PATH);
    }

    @Test
    public void testGetFolloweeCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        assertCountRequestResponse(FollowService.GET_FOLLOWING_COUNT_URL_PATH);
    }

    private void assertCountRequestResponse(String urlPath) throws IOException, TweeterRemoteException {
        UserRequest request = new UserRequest(FakeData.getInstance().getAuthToken(), testUser.getAlias());
        CountResponse response = serverFacade.getUserCount(request, urlPath);
        int followeeCount = FakeData.getInstance().getFakeUsers().size();
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(followeeCount, response.getCount());
    }

    @Test
    public void testGetFollowerCount_missingAuthToken_unauthorizedResponse() {
        assertCountRequestUnauthorized(FollowService.GET_FOLLOWER_COUNT_URL_PATH);
    }

    @Test
    public void testGetFolloweeCount_missingAuthToken_unauthorizedResponse() {
        assertCountRequestUnauthorized(FollowService.GET_FOLLOWING_COUNT_URL_PATH);
    }

    private void assertCountRequestUnauthorized(String urlPath) {
        UserRequest request = new UserRequest(null, testUser.getAlias());

        TweeterRequestException exception = Assertions.assertThrows(TweeterRequestException.class, () -> {
            serverFacade.getUserCount(request, urlPath);
        });

        Assertions.assertEquals("[Unauthorized]", exception.getMessage().substring(0, 14));
    }

    @Test
    public void testGetFollowerCount_missingParameters_invalidRequestResponse() {
        assertCountRequestInvalid(FollowService.GET_FOLLOWER_COUNT_URL_PATH);
    }

    @Test
    public void testGetFolloweeCount_missingParameters_invalidRequestResponse() {
        assertCountRequestInvalid(FollowService.GET_FOLLOWING_COUNT_URL_PATH);
    }

    private void assertCountRequestInvalid(String urlPath) {
        UserRequest request = new UserRequest(FakeData.getInstance().getAuthToken(), null);

        TweeterRequestException exception = Assertions.assertThrows(TweeterRequestException.class, () -> {
            serverFacade.getUserCount(request, urlPath);
        });

        Assertions.assertEquals(exception.getMessage().substring(0, 13), "[Bad Request]");
    }
}
