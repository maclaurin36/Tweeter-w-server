package edu.byu.cs.tweeter.server.dao;

public interface QueueDao {
    void queueBatch(String item);
    void queueStatus(String statusString);
}
