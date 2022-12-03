package edu.byu.cs.tweeter.server.service.action;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.server.dao.FeedDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.DynamoStatus;
import edu.byu.cs.tweeter.server.service.dto.BatchStatusMessage;

public class BatchPostStatusAction {
    private final FeedDao feedDao;

    public BatchPostStatusAction(FeedDao feedDao) {
        this.feedDao = feedDao;
    }

    public void run(SQSEvent batchedJob) {
        for (SQSEvent.SQSMessage postStatusMessage : batchedJob.getRecords()) {
            Gson gson = new Gson();
            BatchStatusMessage batchStatusMessage = gson.fromJson(postStatusMessage.getBody(), BatchStatusMessage.class);
            Status status = batchStatusMessage.getStatus();
            List<String> batchAliases = batchStatusMessage.getAliases();

            feedDao.insertAllStatusToFeed(batchAliases, status);
        }
    }
}
