package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.PaginationObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
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
}
