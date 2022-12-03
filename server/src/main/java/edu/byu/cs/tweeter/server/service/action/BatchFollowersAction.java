package edu.byu.cs.tweeter.server.service.action;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.FollowDao;
import edu.byu.cs.tweeter.server.dao.QueueDao;
import edu.byu.cs.tweeter.server.service.dto.BatchStatusMessage;

public class BatchFollowersAction {
    private final FollowDao followDao;
    private final QueueDao queueDao;

    public BatchFollowersAction(FollowDao followDao, QueueDao queueDao) {
        this.followDao = followDao;
        this.queueDao = queueDao;
    }

    public void processBatch(SQSEvent statusEvent) {
        // Get the status and the alias from the SQSEvent
        for (SQSEvent.SQSMessage postStatusMessage : statusEvent.getRecords()) {
            Gson gson = new Gson();
            Status status = gson.fromJson(postStatusMessage.getBody(), Status.class);

            // Retrieve the list of followers (probably in batches of 500)
            String userAlias = status.getUser().getAlias();
            List<String> followerAliases = followDao.getAllFollowers(userAlias);

            // Create SQS batches of followers to post the status to
            List<String> batchAliases = new ArrayList<>();
            for (String followerAlias : followerAliases) {
                batchAliases.add(followerAlias);
                if (batchAliases.size() == 25) {
                    sendBatch(batchAliases, userAlias, gson);
                    batchAliases = new ArrayList<>();
                }
            }

            if (batchAliases.size() != 0) {
                sendBatch(batchAliases, userAlias, gson);
            }
        }
    }

    private void sendBatch(List<String> batchAliases, String userAlias, Gson gson) {
        BatchStatusMessage statusBatch = new BatchStatusMessage(batchAliases, userAlias);
        String statusBatchString = gson.toJson(statusBatch);
        queueDao.queueBatch(statusBatchString);
    }
}
