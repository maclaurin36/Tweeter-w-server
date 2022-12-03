package edu.byu.cs.tweeter.client.model.service;

import android.os.Looper;
import android.util.Log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.presenter.view.MainActivityView;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.model.net.response.AuthenticateResponse;
import edu.byu.cs.tweeter.model.net.response.PagedResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusServiceTest {

    private CountDownLatch countDownLatch;

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }

    private class CountdownPaginationObserver implements PaginationObserver<Status> {

        private boolean success;
        private String message;
        private List<Status> storyItems;
        private boolean hasMorePages;

        @Override
        public void handleGetListSuccess(List<Status> items, Boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.storyItems = items;
            this.hasMorePages = hasMorePages;
            countDownLatch.countDown();
        }

        @Override
        public void handlePaginationFailure() {
            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            countDownLatch.countDown();
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public List<Status> getStoryItems() {
            return storyItems;
        }

        public boolean isHasMorePages() {
            return hasMorePages;
        }
    }

    @Test
    public void testGetStory_validRequest_observerNotified() throws InterruptedException {
        CountdownPaginationObserver observer = Mockito.spy( new CountdownPaginationObserver());
        StatusService statusService = Mockito.spy(new StatusService());
        User testUser = FakeData.getInstance().getFirstUser();
        resetCountDownLatch();

        Cache cache = Mockito.mock(Cache.class);
        Answer<AuthToken> getAuthTokenAnswer = invocation -> FakeData.getInstance().getAuthToken();
        Mockito.doAnswer(getAuthTokenAnswer).when(cache).getCurrUserAuthToken();

        Cache.setInstance(cache);

        statusService.getStory(testUser, null, observer);
        awaitCountDownLatch();

        Pair<List<Status>, Boolean> pageResults = FakeData.getInstance().getPageOfStatus(null, 10);

        Mockito.verify(observer).handleGetListSuccess(Mockito.anyList(), Mockito.anyBoolean());
        Assertions.assertNull(observer.getMessage());
        Assertions.assertTrue(observer.isSuccess());
        Assertions.assertTrue(observer.isHasMorePages());

        for (int i = 0; i < observer.getStoryItems().size(); i++) {
            Assertions.assertEquals(pageResults.getFirst().get(i), observer.getStoryItems().get(i));
        }
    }

    @Test
    public void testPostStatus_endToEndTest() throws InterruptedException, IOException, TweeterRemoteException, ParseException {
        class MainPresenterCountdown extends MainPresenter {

            public MainPresenterCountdown(MainActivityView view, User user) {
                super(view, user);
            }

            @Override
            public void handlePostStatusSuccess() {
                super.handlePostStatusSuccess();
                countDownLatch.countDown();
            }

            @Override
            public void handleFailure(String message) {
                countDownLatch.countDown();
            }
        }

        ServerFacade serverFacade = new ServerFacade();

        AuthenticateResponse authenticateResponse = doLogin(serverFacade);
        AuthToken authToken = authenticateResponse.getAuthToken();
        User user = authenticateResponse.getUser();

        MainActivityView mainActivityView = Mockito.mock(MainActivityView.class);
        MainPresenter mainPresenter = Mockito.spy(new MainPresenterCountdown(mainActivityView, user));

        String statusMessage = generateRandomString();
        resetCountDownLatch();
        mainPresenter.postStatus(statusMessage);
        awaitCountDownLatch();

        Mockito.verify(mainActivityView).displayInfoMessage("Successfully Posted!");

        Status status = mainPresenter.createStatusFromPost(statusMessage);
        doStoryVerification(authToken, user, serverFacade, status);

    }

    private String generateRandomString() {
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        return new String(array, StandardCharsets.UTF_8);
    }

    private AuthenticateResponse doLogin(ServerFacade serverFacade) throws IOException, TweeterRemoteException {
        LoginRequest loginRequest = new LoginRequest("@Jim", "password");
        AuthenticateResponse authenticateResponse = serverFacade.login(loginRequest, UserService.LOGIN_URL_PATH);
        AuthToken authToken = authenticateResponse.getAuthToken();
        Cache cache = Mockito.mock(Cache.class);
        Cache.setInstance(cache);
        Mockito.doReturn(authToken).when(cache).getCurrUserAuthToken();
        Mockito.doReturn(authenticateResponse.getUser()).when(cache).getCurrUser();
        return authenticateResponse;
    }

    private void doStoryVerification(AuthToken authToken, User user, ServerFacade serverFacade, Status status) throws IOException, TweeterRemoteException {
        PagedRequest<Status> pagedRequest = new PagedRequest<Status>(authToken, user.getAlias(), 1, null);
        PagedResponse<Status> response = serverFacade.getStatusList(pagedRequest, StatusService.GET_STORY_URL_PATH);
        List<Status> statusList = response.getItems();
        Status retrievedStatus = statusList.get(0);
        Assertions.assertEquals(status, retrievedStatus);
    }
}
