package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.PagedRequest;
import edu.byu.cs.tweeter.server.dao.FeedDao;
import edu.byu.cs.tweeter.server.dao.dynamo.bean.DynamoStatus;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class DynamoFeedDao extends DynamoBaseStatusDao implements FeedDao {
    private static final String FEED_TABLE_NAME = "feed";
    private static final DynamoDbTable<DynamoStatus> feedTable = enhancedClient.table(FEED_TABLE_NAME, TableSchema.fromBean(DynamoStatus.class));

    @Override
    protected DynamoDbTable<DynamoStatus> getTableToQuery() {
        return feedTable;
    }

    @Override
    public void insertStatusToFeed(String feedAlias, Status status) {
        DynamoStatus dynamoStatus = new DynamoStatus(feedAlias, status);
        feedTable.putItem(dynamoStatus);
    }
}
