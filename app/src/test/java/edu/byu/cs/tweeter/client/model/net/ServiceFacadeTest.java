package edu.byu.cs.tweeter.client.model.net;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.net.TweeterRequestException;
import edu.byu.cs.tweeter.client.model.net.TweeterServerException;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.request.UserRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.CountResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.model.net.response.UserPagedResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServiceFacadeTest {
    // Test registration
        // Create the request
        // Send the request and wait for the response
        // Make sure that the response is what you expect
    // Test getFollowers
    // Test GetFollowingCount/GetFollowersCount

    private ServerFacade serverFacade = new ServerFacade();

    @Test
    public void testRegister_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        User testUser = FakeData.getInstance().getFirstUser();
        String testPassword = "password";
        RegisterRequest registerRequest = new RegisterRequest(testUser.getAlias(), testUser.getFirstName(), testUser.getLastName(), testUser.getImageUrl(), testPassword);
        AuthenticateResponse response = serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH);

        Assertions.assertTrue(response.isSuccess());
        Assertions.assertNull(response.getMessage());

        User returnedUser = response.getUser();
        Assertions.assertEquals(testUser, returnedUser);
        Assertions.assertNotNull(response.getAuthToken());
    }

    @Test
    public void testRegister_missingParameters_invalidRequestResponse() throws IOException, TweeterRemoteException {
        User testUser = FakeData.getInstance().getFirstUser();
        String testPassword = "password";
        RegisterRequest registerRequest = new RegisterRequest(null, testUser.getFirstName(), testUser.getLastName(), testUser.getImageUrl(), testPassword);
        try {
            serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH);
            Assertions.fail("TweeterRemoteException should have been thrown due to missing user alias");
        }
        catch (TweeterRequestException ex) {
            String errorMessage = ex.getMessage();
            if (!errorMessage.contains("[Bad Request]")) {
                Assertions.fail("Incorrect message response");
            }
        }
        catch (Exception ex) {
            Assertions.fail("Incorrect exception type " + ex.getMessage());
        }
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
        User testUser = FakeData.getInstance().getFirstUser();

        try {
            PagedRequest<String> pagedRequest = new PagedRequest<>(null, testUser.getAlias(), 3, null);
            PagedResponse<User> response = serverFacade.getUserList(pagedRequest, FollowService.GET_FOLLOWERS_URL_PATH);
            Assertions.fail("TweeterRequestException should have been thrown for missing authentication token");
        }
        catch (TweeterRequestException ex) {
            String errorMessage = ex.getMessage();
            if (!errorMessage.contains("[Unauthorized]")) {
                Assertions.fail("Incorrect message response");
            }
        }
        catch (Exception ex) {
            Assertions.fail("Incorrect exception type " + ex.getMessage());
        }
    }

    @Test
    public void testGetFollowers_missingParameters_invalidRequestResponse() {
        User testUser = FakeData.getInstance().getFirstUser();

        try {
            PagedRequest<String> pagedRequest = new PagedRequest<>(FakeData.getInstance().getAuthToken(), null, 3, null);
            serverFacade.getUserList(pagedRequest, FollowService.GET_FOLLOWERS_URL_PATH);
            Assertions.fail("TweeterRequestException should have been thrown for missing alias");
        }
        catch (TweeterRequestException ex) {
            String errorMessage = ex.getMessage();
            if (!errorMessage.contains("[Bad Request]")) {
                Assertions.fail("Incorrect message response");
            }
        }
        catch (Exception ex) {
            Assertions.fail("Incorrect exception type " + ex.getMessage());
        }
    }

    @Test
    public void testGetFollowerCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        User testUser = FakeData.getInstance().getFirstUser();
        UserRequest request = new UserRequest(FakeData.getInstance().getAuthToken(), testUser.getAlias());
        CountResponse response = serverFacade.getUserCount(request, FollowService.GET_FOLLOWER_COUNT_URL_PATH);
        int followerCount = FakeData.getInstance().getFakeUsers().size();
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(followerCount, response.getCount());
    }

    @Test
    public void testGetFolloweeCount_validRequest_correctResponse() throws IOException, TweeterRemoteException {
        User testUser = FakeData.getInstance().getFirstUser();
        UserRequest request = new UserRequest(FakeData.getInstance().getAuthToken(), testUser.getAlias());
        CountResponse response = serverFacade.getUserCount(request, FollowService.GET_FOLLOWING_COUNT_URL_PATH);
        int followeeCount = FakeData.getInstance().getFakeUsers().size();
        Assertions.assertTrue(response.isSuccess());
        Assertions.assertEquals(followeeCount, response.getCount());
    }

    @Test
    public void testGetFollowerCount_missingAuthToken_unauthorizedResponse() {
        User testUser = FakeData.getInstance().getFirstUser();

        try {
            UserRequest request = new UserRequest(null, testUser.getAlias());
            CountResponse response = serverFacade.getUserCount(request, FollowService.GET_FOLLOWER_COUNT_URL_PATH);
            Assertions.fail("TweeterRequestException should have been thrown for missing authentication token");
        }
        catch (TweeterRequestException ex) {
            String errorMessage = ex.getMessage();
            if (!errorMessage.contains("[Unauthorized]")) {
                Assertions.fail("Incorrect message response");
            }
        }
        catch (Exception ex) {
            Assertions.fail("Incorrect exception type " + ex.getMessage());
        }
    }

    @Test
    public void testGetFollowerCount_missingParameters_invalidRequestResponse() {
        User testUser = FakeData.getInstance().getFirstUser();

        try {
            UserRequest request = new UserRequest(FakeData.getInstance().getAuthToken(), null);
            CountResponse response = serverFacade.getUserCount(request, FollowService.GET_FOLLOWER_COUNT_URL_PATH);
            Assertions.fail("TweeterRequestException should have been thrown for missing authentication token");
        }
        catch (TweeterRequestException ex) {
            String errorMessage = ex.getMessage();
            if (!errorMessage.contains("[Bad Request]")) {
                Assertions.fail("Incorrect message response");
            }
        }
        catch (Exception ex) {
            Assertions.fail("Incorrect exception type " + ex.getMessage());
        }
    }

    @Test
    public void testGetFolloweeCount_missingAuthToken_unauthorizedResponse() {
        User testUser = FakeData.getInstance().getFirstUser();

        try {
            UserRequest request = new UserRequest(null, testUser.getAlias());
            CountResponse response = serverFacade.getUserCount(request, FollowService.GET_FOLLOWING_COUNT_URL_PATH);
            Assertions.fail("TweeterRequestException should have been thrown for missing authentication token");
        }
        catch (TweeterRequestException ex) {
            String errorMessage = ex.getMessage();
            if (!errorMessage.contains("[Unauthorized]")) {
                Assertions.fail("Incorrect message response");
            }
        }
        catch (Exception ex) {
            Assertions.fail("Incorrect exception type " + ex.getMessage());
        }
    }

    @Test
    public void testGetFolloweeCount_missingParameters_invalidRequestResponse() {
        User testUser = FakeData.getInstance().getFirstUser();

        try {
            UserRequest request = new UserRequest(FakeData.getInstance().getAuthToken(), null);
            CountResponse response = serverFacade.getUserCount(request, FollowService.GET_FOLLOWING_COUNT_URL_PATH);
            Assertions.fail("TweeterRequestException should have been thrown for missing authentication token");
        }
        catch (TweeterRequestException ex) {
            String errorMessage = ex.getMessage();
            if (!errorMessage.contains("[Bad Request]")) {
                Assertions.fail("Incorrect message response");
            }
        }
        catch (Exception ex) {
            Assertions.fail("Incorrect exception type " + ex.getMessage());
        }
    }
}
