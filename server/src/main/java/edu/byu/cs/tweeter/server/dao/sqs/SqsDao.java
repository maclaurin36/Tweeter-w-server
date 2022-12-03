package edu.byu.cs.tweeter.server.dao.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import edu.byu.cs.tweeter.server.dao.QueueDao;

public class SqsDao implements QueueDao {
    private static final String JOB_QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/424513167374/batchQueue";
    private static final String STATUS_QUEUE_URL = "https://sqs.us-west-1.amazonaws.com/424513167374/statusQueue";
    private static final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    @Override
    public void queueBatch(String batchString) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(JOB_QUEUE_URL)
                .withMessageBody(batchString);
        sqs.sendMessage(send_msg_request);
    }

    @Override
    public void queueStatus(String statusString) {
        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(STATUS_QUEUE_URL)
                .withMessageBody(statusString);
        sqs.sendMessage(send_msg_request);
    }
}
